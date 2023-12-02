# Tistory

### 기능 정리
 1. Tistory 폴더 생성 및 github 연동
 2. 프로젝트 생성하고 기본 틀 구성 후 시큐리티 로직 구현
 3. AOP 구현 및 사용자 프로필 사진 업데이트 기능 추가
 4. 사용자 정보 업데이트 기능 추가
 5. 사용자 패스워드 변경시 예외처리 보완 및 카테고리 생성 API 추가
 6. 카테고리 List 불러오기 API 추가 후 ajax로 bodyHeader에 동적으로 뿌려주는 기능 추가
 7. 카테고리 List 불러오기 API 응답 값 DTO로 변경 및 post 글쓰기 화면에 카테고리 선택 셀렉트 박스에도 동적으로 뿌려주는 기능 추가
 8. 내 포스팅 리스트 보기 API 구현 및 카테고리 리스트 불러오기 서비스 예외처리 부분 수정(초기 설정시 문제 발생)
 9. 글 쓰기, 글 수정(이미지 업로드 로직 추가) 기능 추가 및 인증 예외 처리 부분은 User 부분만 남겨놓고 나머지는 ajax에서 예외처리 제외
10. 전체 포스팅 리스트 보기 API 구현 및 인증 없이도 보이게끔 화면에 구현하고 포스팅을 클릭하면 그 포스팅에 대한 id 정보, pageOwner id 정보, 현재 로그인한 User의 id 정보를 들고 가게끔 구현
11. 각 도메인에 날짜 항목에 @JsonFormat(pattern = yyyy-MM-dd HH:mm) 추가
12. a 태그에 박혀 있는 기본 href 제거 작업 및 readPostInfo 메소드 매개변수를 User엔티티로 받았던걸 User엔티티의 id로 받게끔 변경
13. 좋아요 API 만들기 및 화면 구성(화면 깜빡임 없이 구현)
14. 게시판 화면 boardList.html, boardWriteForm.html 만들고 컨트롤러 연결 및 게시판 글 쓰기, 게시판 글 리스트 조회, 게시판 글 리스트 조회(페이징)
15. 게시판 글쓰기 파일 업로드, 게시글 페이징 처리(글쓰기 서비스 부분 save 위치 수정)