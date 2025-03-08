# 🐳 Docker 이미지 Import 및 실행 방법

이 문서는 저장된 Docker 이미지(`.tar` 파일)를 다른 PC에서 Import하고, 컨테이너를 실행하는 방법을 설명합니다.  
**MariaDB 및 Redis 컨테이너를 실행하는 방법을 포함**하고 있습니다.

---

## 📌 1. Docker 이미지 가져오기 (Import)
집에 있는 PC에서 Docker를 실행한 후, `.tar` 파일을 가져와서 Docker에 등록합니다.

1. **Docker Desktop 실행**
2. **PowerShell 또는 CMD 실행**
3. **이미지가 저장된 폴더로 이동**
   ```sh
   cd C:\docker-images
   ```
4. **Docker에 이미지 로드**
   ```sh
   docker load -i my-mariadb-image.tar
   docker load -i my-redis-image.tar
   ```
5. **이미지가 정상적으로 등록되었는지 확인**
   ```sh
   docker images
   ```

✅ `my-mariadb-image` 와 `my-redis-image` 가 목록에 나오면 성공! 🎉

---

## 📌 2. MariaDB 컨테이너 실행
이제 가져온 **MariaDB 이미지로 컨테이너를 실행**합니다.

```sh
docker run -d \
  --name my-mariadb \
  -p 3306:3306 \
  my-mariadb-image
```

✅ **컨테이너 실행 확인**
```sh
docker ps
```
🚀 `my-mariadb` 컨테이너가 실행 중이면 성공!

---

## 📌 3. Redis 컨테이너 실행
이제 가져온 **Redis 이미지로 컨테이너를 실행**합니다.

```sh
docker run -d \
  --name my-redis \
  -p 6379:6379 \
  my-redis-image
```

✅ **컨테이너 실행 확인**
```sh
docker ps
```
🚀 `my-redis` 컨테이너가 실행 중이면 성공!

---

## 📌 4. DBeaver에서 MariaDB 연결
1. **DBeaver 실행**
2. **상단 메뉴 → "Database" → "New Connection" 클릭**
3. **MariaDB 선택 → "Next" 클릭**
4. **아래 정보 입력**
    - **Host**: `localhost`
    - **Port**: `3306`
    - **Database**: (비워둬도 됨)
    - **Username**: `root`
    - **Password**: `1234`
5. **"Test Connection" 클릭**
    - 성공하면 "Finish" 클릭 후 연결 완료! 🎉

✅ **DBeaver에서 MariaDB 연결 성공!** 🚀

---

## 📌 5. Redis 접속 테스트 (`redis-cli`)
1. **PowerShell 또는 CMD 실행**
2. **Redis 서버에 접속**
   ```sh
   redis-cli -h localhost -p 6379
   ```
3. **간단한 명령어 테스트**
   ```sh
   PING
   ```
   **출력 결과**
   ```
   PONG
   ```
✅ `PONG`이 나오면 Redis 접속 성공! 🎉

---

## 🚀 **마무리**
이제 MariaDB와 Redis가 정상적으로 실행되고,  
DBeaver와 Redis CLI에서 모두 접속할 수 있습니다! 🎉

혹시 문제가 발생하면 `docker ps`를 실행하여 컨테이너가 정상적으로 실행 중인지 확인하세요.  
⚡ **Docker와 함께 즐거운 개발하세요!** 😊🚀
