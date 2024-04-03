#!/bin/bash

# 현재 디렉토리에서 .png 확장자를 가진 모든 파일을 찾아서 처리합니다.
find . -type f -name "*.png" -exec sh -c '
  for file do
    # 확장자를 제외한 파일명을 얻기 위해 basename 사용
    dirname=$(dirname "$file")
    filename=$(basename "$file" .png)
    # 대문자로 변경
    newfilename=$(echo "$filename" | tr "[:lower:]" "[:upper:]")
    # 파일 이동 (이름 변경)
    mv "$file" "$dirname/${newfilename}.png"
  done
' sh {} +
