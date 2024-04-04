#!/bin/bash

# 현재 디렉토리의 'Rank=*.png' 패턴을 가진 모든 파일에 대해 반복
for file in Rank=*.png; do
  # 파일의 기본 이름(확장자 없음)을 추출하고, 'Rank='를 제거한 다음 대문자로 변환
  base_name=$(echo "$file" | sed 's/Rank=//g' | cut -d. -f1 | tr '[:lower:]' '[:upper:]')
  # 파일의 확장자만 추출
  extension=$(echo "$file" | sed 's/.*\.//g' | tr '[:upper:]' '[:lower:]')

  # 새 파일 이름 구성: 기본 이름 + '.' + 소문자 확장자
  newname="${base_name}.${extension}"

  # 파일 이름 변경
  mv "$file" "$newname"

  echo "Renamed $file to $newname"
done
