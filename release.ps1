#!/usr/bin/env pwsh
$ErrorActionPreference = "Stop"

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
