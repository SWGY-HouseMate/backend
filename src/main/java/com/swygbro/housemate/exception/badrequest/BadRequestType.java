package com.swygbro.housemate.exception.badrequest;

public enum BadRequestType {

    DEFAULT("잘못된 요청 데이터 입니다. 파라미터를 확인해 주세요."),
    KAKAO_LOGIN_COMMUNICATION_FAIL("카카오 로그인 요청중 통신에 실패하였습니다."),
    GOOGLE_LOGIN_COMMUNICATION_FAIL("구글 로그인 요청중 통신에 실패하였습니다."),
    ;

    private final String message;

    BadRequestType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}