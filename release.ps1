#!/usr/bin/env pwsh
$ErrorActionPreference = "Stop"

function Show-Usage {
    param([string]$Out = "stdout")
    $lines = @(
        "Usage: $PSCommandPath [-m <message> | --message <message> | --message=<message>]",
        "",
        "  -m, --message <message>  Use this message as the tag/release note (non-interactive).",
        "  -h, --help               Show this help and exit.",
        "",
        "With no -m/--message, the message is read interactively:",
        "type the message and press Enter twice when done (interactive mode)."
    )
    if ($Out -eq "stderr") {
        foreach ($l in $lines) { [Console]::Error.WriteLine($l) }
    } else {
        foreach ($l in $lines) { Write-Output $l }
    }
}

$MessageText = ""
$HaveMessage = $false

$i = 0
while ($i -lt $args.Count) {
    $a = $args[$i]
    if ($a -eq "-m" -or $a -eq "--message") {
        if ($i + 1 -ge $args.Count) {
            [Console]::Error.WriteLine("Error: option '$a' requires an argument")
            Show-Usage -Out "stderr"
            exit 2
        }
        $MessageText = $args[$i + 1]
        $HaveMessage = $true
        $i += 2
    } elseif ($a -like "--message=*") {
        $MessageText = $a.Substring("--message=".Length)
        $HaveMessage = $true
        $i += 1
    } elseif ($a -eq "-h" -or $a -eq "--help") {
        Show-Usage
        exit 0
    } else {
        [Console]::Error.WriteLine("Error: unknown argument: $a")
        Show-Usage -Out "stderr"
        exit 2
    }
}

$LatestTag = git tag --list 'v*' --sort=-v:refname | Select-Object -First 1

if ([string]::IsNullOrWhiteSpace($LatestTag)) {
    $NewTag = "v0.1"
} else {
    $Version = $LatestTag -replace '^v', ''
    $Parts = $Version -split '\.'
    $Major = [int]$Parts[0]
    $Minor = if ($Parts.Count -gt 1) { [int]$Parts[1] } else { 0 }
    $Minor++
    $NewTag = "v$Major.$Minor"
}

Write-Host "Next tag: $NewTag"

if (-not $HaveMessage) {
    Write-Host "Enter tag message (press Enter twice when done):"

    $Message = @()
    while ($true) {
        $Line = Read-Host
        if ([string]::IsNullOrWhiteSpace($Line) -and $Message.Count -gt 0) {
            break
        }
        $Message += $Line
    }

    $MessageText = $Message -join "`n"
}

if ([string]::IsNullOrWhiteSpace($MessageText)) {
    Write-Error "Tag message cannot be empty"
    exit 1
}

git tag -a $NewTag -m $MessageText
git push --tags

# Cleanup: delete all local tags except the newly created one
Write-Host "Cleaning up local tags..."
$AllTags = git tag --list 'v*'
foreach ($tag in $AllTags) {
    if ($tag -ne $NewTag) {
        git tag -d $tag | Out-Null
    }
}

Write-Host "Released: $NewTag"
