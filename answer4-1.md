# 객실 예약 API
## Request
```http request
POST /v1/hotel-api/users/{user-id}
Content-Type: application/json

{
  "startDate" : "2023-06-23",
  "endDate" : "2023-06-25",
  "hotelName" : "FirstHotel",
  "roomName" : "TwinRoom1"
}
```
## Response
1. 예약 성공
```http request
HTTP/1.1 201
Content-Type: application/json

{
  "userId" : "100"
  "startDate" : "2023-06-23",
  "endDate" : "2023-06-25",
  "hotelName" : "FirstHotel",
  "roomName" : "TwinRoom1"
}
```

2. 예약할 수 없는 사람이 객실 예약 API 를 호출한 경우

```http request
HTTP/1.1 400 
Content-Type: application/json

{
  "errorMessage" : "예약 불가능한 사용자입니다." 
}
```

3. 하루에 3개 이상 객실을 초과한 경우
```http request
HTTP/1.1 400
Content-Type: application/json

{
  "errorMessage" : "하루 최대 3개까지만 예약가능합니다." 
}
```

4. 이미 예약된 객실인 경우
```http request
HTTP/1.1 409
Content-Type: application/json

{
  "errorMessage" : "이미 예약된 객실입니다." 
}
```


