#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "Usage: $0 [-m <message> | --message <message> | --message=<message>]"
  echo ""
  echo "  -m, --message <message>  Use this message as the tag/release note (non-interactive)."
  echo "  -h, --help               Show this help and exit."
  echo ""
  echo "With no -m/--message, the message is read from stdin:"
  echo "type or pipe the message and press Ctrl+D when done (interactive mode)."
}

MESSAGE=""
HAVE_MESSAGE=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    -m|--message)
      if [[ $# -lt 2 ]]; then
        echo "Error: option '$1' requires an argument" >&2
        exit 2
      fi
      MESSAGE="$2"
      HAVE_MESSAGE=1
      shift 2
      ;;
    --message=*)
      MESSAGE="${1#--message=}"
      HAVE_MESSAGE=1
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "Error: unknown argument: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
done

LATEST_TAG=$(git tag --list 'v*' --sort=-v:refname | head -n 1)

if [[ -z "$LATEST_TAG" ]]; then
  NEW_TAG="v0.1"
else
  VERSION="${LATEST_TAG#v}"
  MAJOR="${VERSION%%.*}"
  MINOR="${VERSION##*.}"
  MINOR=$((MINOR + 1))
  NEW_TAG="v${MAJOR}.${MINOR}"
fi

echo "Next tag: ${NEW_TAG}"

if [[ "$HAVE_MESSAGE" -eq 0 ]]; then
  echo "Enter tag message (press Ctrl+D when done):"
  MESSAGE=$(cat)
fi

if [[ -z "$MESSAGE" ]]; then
  echo "Error: Tag message cannot be empty" >&2
  exit 1
fi

git tag -a "${NEW_TAG}" -m "${MESSAGE}"
git push --tags

# Cleanup: delete all local tags except the newly created one
echo "Cleaning up local tags..."
git tag --list 'v*' | while read -r tag; do
  if [[ "$tag" != "$NEW_TAG" ]]; then
    git tag -d "$tag" >/dev/null
  fi
done

echo "Released: ${NEW_TAG}"
