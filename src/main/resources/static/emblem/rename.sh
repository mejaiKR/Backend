for file in Rank=*.png; do
  mv "$file" "${file//Rank=}"
done

