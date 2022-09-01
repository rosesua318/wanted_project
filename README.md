## 원티드 클론 코딩 - 라이징테스트 8th  / 원티드 A - Server

#### [22/08/20 - 22/09/02] 2주간 진행
 
#### [sua/김민서] [화니/김영환]

## 2022.08.20
#### 0. 기획서 작성
#### 1. ERD 설계 - https://aquerytool.com/aquerymain/index/?rurl=f947f590-d659-4ca8-b075-c09e4db79a41 (비밀번호 : 7j1kcm) 
#### 2. EC2 & RDS 환경 구축 완료
#### 3. API 명세서 리스트업 완료 (약 40개 작성)
#### 4. dev/prod 서버 구축 (실직적으로 prod 서버 사용할 예정)
#### 5. SSL 구축
#### 6. API 역할 분담
#### 7. 더미데이터 (매우 기본적인 데이터만 추가)


## 2022.08.21
#### 1. 회원 API 개발 완료
#### 2. 좋아요 API 개발 완료
#### 3. 북마크 API 개발 90% 완료
#### 4. 더미데이터 - 데이터 조금 추가하여 테스트 진행
#### 5. 홈 화면 조회 API 개발 완료
#### 6. ERD 수정
#####   - Banner 테이블에 type 컬럼 추가
#####   - 각 종 테이블 default 값 추가
#####   - Company 테이블에 logoUrl 컬럼 추가
#### 7. 추가된 API 모두 명세서, 서버 반영 완료
#### 8. 깃헙 merge 이슈
#####    - BaseResponseStatus 에서 같은 에러 코드를 사용하여 발생
#####    - 각자 사용할 번호를 나누어 해결
#### 9. 서버 반영 시 빌드가 되지 않는 에러
#####  - build.gradle 수정으로 해결
#### 10. 클라이언트 개발자와 협의하여 API 우선순위 수정 -> 홈 화면 부터


## 2022.08.22
#### 1. 서브도메인 SSL 인증 문제 발생 -> nginx 설정에서 SSL 블록 추가해주어서 해결
#### 2. CORS 문제 발생 -> CORS 방침 설정해줌 (WebMvcConfig.java) 
#### 3. 홈 화면 조회 API에서 클라이언트 요청으로 배너 인덱스도 함께 보내도록 수정 완료
#### 4. 북마크, 채용 페이지 관련 조회에서 이미지가 여러개 출력되어버리는 에러 발생
#####     - 북마크 API 구조 변경 진행 중 - > 수정 완료.
#### 5. 채용 중인 포지션 조회하기 API 진행 중 
#### 6. 채용 중인 페이지 조회(회원,비회원) API 진행 중 (70%) -> 완료
#### 7. 더미데이터 대량 추가 (채용,회사,북마크,태그 등)
#### 8. 채용 중인 포지션 조회하기 클라이언트와 협의하여 구현 파트 협의
#### 9. 수정 완료한 API 명세서, 서버 반영 
#### 10. 채용중인 포지션 조회하기 (비회원용) API  
#### 11. 채용중인 포지션 조회하기 (회원용) API 완성
#### 12. 채용 정보 조회(상세페이지) API 완료.
#### 13. 직군, 직무, 지역, 상세지역, 기술스택 조회 API 

## 1차 피드백
#### 1. 깃 헙 Branch 사용, 이슈 정리
##### 도메인 별로 각자 Branch 사용.  코드 리뷰 후 main과 merge

## 2022.08.23
#### 1. 회사 태그 검색 회원/비회원용 API 완성
#### 2. 추천 태그 조회 API 완성
#### 3. 추천 태그 클릭 검색 회원/비회원용 API  완성
#### 4. 기업 태그 홈 조회 회원/비회원용 API 완성
#### 5. 회사 정보 조회 회원용/비회원용 API 완료
#### 6. 회사 뉴스 조회하기 API 완료
#### 7. Follow 테이블 추가 (팔로우 기능 추가 위함)
#### 8. ERD 수정(오타 등)
#### 9. 태그 외 검색 회원/비회원용 API 완성
#### 10. 검색 기록 & 추천 태그 조회 회원용 API 완성
#### 11. 검색 기록 삭제 API 

## 2022.08.24
#### 1. 테이블 생성 InterestTag(관심태그 대분류), InterestClassitifcation(관심태그 분류), UserInterestTag(사용자 관심태그 설정), Specialty(전문 분야 설정), SpecialtySkill(전문 분야 스킬 설정)
#### 2. My 원티드 조회 API 
#### 3. 지원 현황 조회 (작성중, 지원한 포지션) API 완성
#### 4. 지원 현황 검색 - 회사명 검색 (작성중, 지원한 포지션) API 완성
#### 5. 관심 태그 설정 조회 API 완성
#### 6. 관심 태그 설정 API 완성 
#### 7. 포인트 조회 API 완성
#### 8. 포인트 조회 API에서 만료일자 출력 형식 변경
#### 9. 알림 조회 API 완성
#### 10. 알림 설정 조회 API 완성
#### 11. 알림 설정 API 완성 
#### 12. 이력서 리스트 조회 API 완료
#### 13. 이력서 상세 조회 API 완료

## 2022.08.25
#### 1. User테이블에 isNickname (커뮤니티 프로필 닉네임 설정 여부), nickname VARCHAR(8) 컬럼 추가
#### 2. Posting 테이블 추가 (postingIdx, title, content, imageUrl, createdAt, updatedAt, status, userIdx) - 커뮤니티 게시글 테이블
#### 3. Comment 테이블 추가 (commentIdx, content, userIdx, positingIdx, createdAt, status) - 커뮤니티 댓글 테이블
#### 4. LikePost 테이블 추가 (likePostIdx, positingIdx, userIdx, status) - 커뮤니티 게시글 좋아요 테이블
#### 5. CommunityTag 테이블 추가 (ctIdx, name) (추천,전체,회사생활 등) - 커뮤니티 태그 테이블
#### 6. PostingTag 추가 (ptIdx, postingIdx, ctIdx) - Posting과 CommunityTag 연결 테이블
#### 7. WantedDao 오류 수정 (null 결과값을 위해 try-catch문 씀)
#### 8. 커뮤니티 기능을 위한 더미데이터 삽입 (게시글, 댓글, 커뮤니티 태그)
#### 9. 커뮤니티 추천, 전체 탭을 제외한 탭 조회하기 회원/비회원 API 
#### 10. 커뮤니티 추천, 전체 탭을 제외한 탭 조회하기 회원 API에서 응답값 사용자 정보 추가
#### 12. 이력서에 관련된 모든 테이블에 status 컬럼 추가
#### 13. 이력서 상세 내용 생성 / 삭제 API (경력/경력성과/학력/수상및기타/외국어/어학시험/링크)완료
#### 14. 이력서 작성 및 수정 API 완료
#### 15. 이력서 삭제 API 완료
#### 16. 커뮤니티 전체 탭 조회하기 회원/비회원 API 완성
#### 17. 커뮤니티 추천 탭 조회하기 회원/비회원 API 완성

## 2022.08.26
#### 1. 클라이언트 요청으로 채용보상금 String -> int로 전달 타입 변경
#### 2. 커뮤니티 게시글 상세 조회하기 회원/비회원 API 완성
#### 3. 커뮤니티 프로필 설정 조회 API 완성
#### 4. 커뮤니티 프로필 닉네임 설정 변경 API 완성
#### 5. Applicant 테이블에 전화번호,이메일 등의 컬럼 추가
#### 6. 프로필 이미지 변경 API 완료 -> s3 반영하여 수정할 예정
#### 7. 계정 비공개/공개설정 API 완료
#### 8. 지원하기 API 
#### 9. S3 버킷 생성 후, 스프링부트 S3 설정 코드 작성
#### 10. 커뮤니티 게시글 작성하기 API 완성 (S3 사용하여 이미지 업로드)
#### 11. 커뮤니티 게시글 삭제하기 API 완성
#### 12. 커뮤니티 게시글 수정하기 API 완성 (S3 사용하여 이미지 업로드)
#### 13. 커뮤니티 댓글 작성하기 API 완성
#### 14. 커뮤니티 댓글 삭제하기 API 완성

## 2022.08.27
#### 1. 커뮤니티 게시글 좋아요 등록 API 완성
#### 2. 커뮤니티 게시글 좋아요 삭제 API 완성
#### 3. My 커뮤니티 조회 (작성글 탭) API 완성
#### 4. My 커뮤니티 조회 (작성댓글 탭) API 완성
#### 5. My 커뮤니티 조회 (좋아요 탭) API 완성
#### 6. User 테이블에 구직 여부 컬럼 추가
#### 7. 구직여부 설정 API 완료
#### 8. 전문분야 설정,조회 API 완료
#### 9. User 관련 리팩토링 1차 -> 트랜잭션 적용, 이메일/비밀번호 정규식 적용 등 전체적인 코드 개선

## 2022.08.28
#### 1. 좋아요 기능 API jwt 적용
#### 2. User 관련 리팩토링 2차 -> validation 추가(정규식 반영 등)
#### 3. 북마크 관련 리팩토링 1차 -> JWT, 트랜잭션 적용 등

## 2022.08.29
#### 1. 북마크 관련 리팩토링 2차 -> DTO 클래스 개선
#### 2. Company 관련 리팩토링 1차 -> DTO 클래스 개선
#### 3. 관심 태그 설정 API validation 추가 (request 값 충족 못할시에 대한 validation)

## 2022.08.30
#### 1. 북마크 삭제 시 채용중인 포지션 조회하기에서 적용되게 코드 수정
#### 2. 지원 현황 검색 validation 추가
#### 3. User 관련 리팩토링 3차 -> DTO 개선

## 2022.08.31
#### 1. 검색하기 기능 API validation 
#### 2. Follow 리팩토링 
#### 3. 소셜로그인 반영(카카오)
#### 4. 게시글 작성 기능 API 클라이언트 요청으로 request 타입 requestParam에서 requestPart로 변경 (formdata로 request 받을 수 있도록)
#### 6. 게시글 작성, 수정 API 전체 태그 추가되게 수정 
#### 7. Employment 리팩토링

## 2022.09.01
#### 1. 게시글 수정 태그 조회 API 완성

## 2022.09.02
#### 1. 시연 영상 
