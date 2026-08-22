#!/usr/bin/env bash
set -euo pipefail

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
echo "Enter tag message (press Ctrl+D when done):"

MESSAGE=$(cat)

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
