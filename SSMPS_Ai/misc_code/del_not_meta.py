import os

folder_path = r"C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\상품 이미지\image" # 폴더 경로 입력
for filename in os.listdir(folder_path):
    if "jpg" not in filename:
        file_path = os.path.join(folder_path, filename)
        os.remove(file_path)
        # print(f"Removed {file_path}")