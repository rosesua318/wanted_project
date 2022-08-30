package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_USER_IDX(false,2004,"유저 인덱스가 일치하지 않습니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_USERS_EMPTY_PASSWORD(false,2018,"비밀번호를 입력해주세요."),

    POST_USERS_EMPTY_ID(false,2019,"아이디를 입력해주세요."),



    POST_USERS_EMPTY_PHONE(false,2020,"전화번호를 입력해주세요."),

    POST_USERS_EXISTS_PHONE(false,2021,"중복된 전화번호입니다."),

    POST_USERS_INVALID_PASSWORD(false,2022,"비밀번호는 특수문자,문자,숫자 포함 8~20자리만 가능합니다."),


    INVALID_USER_INACTIVE(false,2023,"탈퇴한 회원이거나 정보가 없는 회원입니다."),

    INVALID_JOBSEARCH(false,2024,"해당하는 구직 여부 설정값이 없습니다."),

    POST_USERS_EMPTY_NAME(false,2025,"회원 이름을 입력해주세요."),



    // 수아님
    POST_POSTING_NO_TITLE(false, 2101, "제목이 없습니다"),
    POST_POSTING_NO_CONTENT(false, 2102, "내용이 없습니다"),

    FAIL_IMAGE_UPLOAD(false, 2103, "이미지 업로드에 실패했습니다"),
    
    POST_POSTING_NO_TAG(false, 2104, "태그가 없습니다"),


    // [POST] / bookmark
    POST_BOOKMARK_CREATE_FAIL(false,2030,"북마크 할 채용 포지션을 선택해주세요."),

    POST_BOOKMARK_EXISTS_EMPLOYMENT(false,2031,"이미 북마크 된 채용 포지션 입니다."),
    // POST / FOLLOW

    PATCH_LIKES_NO_DATA(false,2032,"좋아요가 되어있지 않습니다."),

    POST_FOLLOW_EXISTS(false,2033,"이미 팔로우된 회사입니다."),

    PATCH_FOLLOW_NO_DATA(false,2034,"팔로우가 되어있지 않은 상태입니다."),

    POST_RESUMETABLE_NOT_EXISTS(false,2035,"생성되어있지 않은 이력서입니다."),

    PATCH_SEARCH_RECORDS_NO_DATA(false, 2100, "없는 검색 기록을 삭제하려고 합니다."),

    PATCH_POSTING_NO_DATA(false, 2105, "없는 게시글입니다."),

    PUT_POSTING_NO_DATA(false, 2106, "없는 게시글입니다."),

    POST_COMMENT_NO_CONTENT(false, 2107, "댓글 내용이 없습니다."),

    POST_POSTING_NO_DATA(false, 2108, "없는 게시글입니다."),

    PATCH_COMMENT_NO_DATA(false, 2109, "없는 댓글입니다."),

    PATCH_LIKES_POSTING_NO_DATA(false, 2110, "게시글 좋아요가 되어있지 않습니다"),

    PATCH_INTEREST_TAG_NO_DATA(false, 2111, "설정할 관심 태그가 없습니다"),

    POST_SEARCH_NO_KEYWORD(false, 2112, "검색할 키워드를 입력하지 않았습니다"),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    MODIFY_FAIL_STATUS(false,4013,"회원 상태 변경에 실패하였습니다."),

    MODIFY_FAIL_LIKES(false,4014,"좋아요 상태 변경에 실패하였습니다."),

    MODIFY_FAIL_PWD(false,4015,"비밀번호 변경에 실패하였습니다."),

    CREATE_FAIL_RESUMETABLE(false,4016,"이력서 테이블(요소) 생성에 실패하였습니다."),

    DELETE_FAIL_RESUMETABLE(false,4017,"이력서  테이블(요소) 삭제에 실패하였습니다."),

    MODIFY_FAIL_USER_IMAGE(false,4018,"유저 프로필 이미지 변경 실패하였습니다."),

    CREATE_FAIL_APPLICANT(false,4019, "지원하기에 실패했습니다."),
    MODIFY_FAIL_BOOKMARK_STATUS(false,4030,"북마크 삭제에 실패하였습니다."),

    MODIFY_FAIL_RESUMETABLE_STATUS(false,4031,"이력서 테이블 삭제에 실패하였습니다."),

    UPDATE_FAIL_RESUMEINFO(false,4032,"이력서 기본컬럼 작성에 실패하였습니다."),

    UPDATE_FAIL_CAREER(false,4033,"이력서 경력 작성에 실패하였습니다."),

    UPDATE_FAIL_CAREERESULT(false,4044,"이력서 경력 성과 작성에 실패하였습니다."),

    UPDATE_FAIL_EDUCATION(false,4045,"이력서 학력 작성에 실패하였습니다."),

    UPDATE_FAIL_LANGUAGE(false,4046,"이력서 외국어 작성에 실패하였습니다."),

    UPDATE_FAIL_TEST(false,4047,"이력서 어학시험 작성에 실패하였습니다."),

    UPDATE_FAIL_LINK(false,4048,"이력서 링크 작성에 실패하였습니다."),

    UPDATE_FAIL_AWARD(false,4049,"이력서 수상 작성에 실패하였습니다."),

    DELETE_FAIL_RESUME(false,4050,"이력서 삭제에 실패했습니다."),

    MODIFY_FAIL_USER_PRIVATE(false,4051,"계정 상태 변경에 실패했습니다.(비공개/공개)"),

    UPDATE_FAIL_SPECIALTY_SKILL(false,4052,"스킬 작성에 실패했습니다.(전문분야)"),

    MODIFY_FAIL_JOBSEARCH_STATUS(false,4053,"구직 여부 변경에 실패했습니다."),




    // 수아님
    SEARCH_TAG_NO_DATA(false, 4100, "존재하지 않는 태그입니다."),

    SET_ALARM_WRONG_NUMBER(false, 4101, "형식이 맞지 않는 설정값입니다."),

    ;



    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
