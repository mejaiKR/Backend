import requests
import time
from PIL import Image
from io import BytesIO

# 이미지 URL 및 파일 확장자 설정
url = "https://ddragon.leagueoflegends.com/cdn/14.5.1/img/profileicon/"
ext = ".png"

# 0부터 5999까지 이미지 다운로드 시도
for i in range(4780, 8000):
    # 이미지 URL 구성
    image_url = f"{url}{i}{ext}"

    try:
        # 이미지 요청
        response = requests.get(image_url)

        # 요청이 성공했는지 확인 (HTTP 상태 코드 200)
        if response.status_code == 200:
            # 이미지 데이터로부터 PIL 이미지 객체 생성
            img = Image.open(BytesIO(response.content))

            # 이미지 파일로 저장
            img.save(f"image{i}{ext}")
        else:
            print(f"Image not found: {image_url}")
    except Exception as e:
        print(f"Error downloading {image_url}: {e}")

    # 서버에 부담을 주지 않기 위해 각 요청 사이에 약간의 지연 시간 추가
    time.sleep(0.5)
