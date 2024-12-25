import boto3
import requests
import re
import time
import json
import datetime
from io import BytesIO
from PIL import Image
from botocore.exceptions import ClientError


def fetch_latest_version():
    versions_url = "https://ddragon.leagueoflegends.com/api/versions.json"
    versions_response = requests.get(versions_url)
    versions = versions_response.json()
    return versions[0]  # 첫 번째 요소가 최신 버전

def get_start_index():
    try:
        response = s3.get_object(Bucket=bucket_name, Key='last_index.json')
        file_content = response['Body'].read().decode('utf-8')
        data = json.loads(file_content)

        start_index = data.get('last_index', 0)
        print(f"Starting index: {start_index}")
        return start_index
    except Exception as e:
        print(f"Error listing objects in S3: {e}")
    return 0


latest_version = fetch_latest_version()
print(f"Latest version: {latest_version}")

def get_icon_list():
    icon_list = f'https://ddragon.leagueoflegends.com/cdn/{latest_version}/data/ko_KR/profileicon.json'
    response = requests.get(icon_list)
    if not response.ok:
        print(f"Error fetching DDragon data: {response.status_code}")
        return
    data_json = response.json()
    icons_data = data_json.get("data", {})

    sorted_keys = sorted(icons_data.keys(), key=lambda x: int(x))
    start_index = get_start_index()

    filtered_icons_data = {
        k: v
        for k, v in icons_data.items()
        if int(k) >= start_index
    }

    return filtered_icons_data




bucket_name = 'mejai'
s3_prefix = 'profileIcon/'
ext = '.png'
s3 = boto3.client('s3')

# -------------------------------------------------------------------
# 3. S3에 이미 존재하는 아이콘 중 가장 큰 index 확인
#    (예: profileIcons/1234.png -> 1234)
# -------------------------------------------------------------------

def upload_last_index(last_index):
    try:
        s3.put_object(
            Bucket=bucket_name,
            Key='last_index.json',
            Body=json.dumps({'last_index': last_index}),
            ContentType='application/json'
        )
        print(f"Last index updated: {last_index}")
    except Exception as e:
        print(f"Error updating last index: {e}")


def upload_icon():
    data = get_icon_list()
    if not data:
        return

    last_index = get_start_index()
    for key in data:
        try:
            base_icon_url = f"https://ddragon.leagueoflegends.com/cdn/{latest_version}/img/profileicon/"
            image_url = f"{base_icon_url}{key}{ext}"
            response = requests.get(image_url)

            if response.status_code == 200:
                img = Image.open(BytesIO(response.content))

                buffer = BytesIO()
                img.save(buffer, format="PNG")
                buffer.seek(0)

                file_name = f"{s3_prefix}{key}{ext}"

                s3.put_object(
                    Bucket=bucket_name,
                    Key=file_name,
                    Body=buffer,
                    ContentType='image/png'
                )
                last_index = max(last_index, int(key))
                print(f"Image uploaded to S3: {file_name}")
            else:
                print(f"Image not found: {image_url}")
        except Exception as inner_e:
            print(f"Error downloading or uploading {image_url}: {inner_e}")
    upload_last_index(last_index)

upload_icon()
