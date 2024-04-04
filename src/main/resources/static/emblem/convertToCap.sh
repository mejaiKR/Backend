
# 현재 디렉토리의 'Rank=*.png' 패턴을 가진 모든 파일에 대해 반복
for file in Rank=*.png; do
  # 'Rank='를 제외한 나머지 파일 이름을 추출하고 대문자로 변환
  newname=$(echo "$file" | sed 's/Rank=//g' | tr '[:lower:]' '[:upper:]')

  # 파일 이름 변경
  mv "$file" "$newname"

  echo "Renamed $file to $newname"
done
