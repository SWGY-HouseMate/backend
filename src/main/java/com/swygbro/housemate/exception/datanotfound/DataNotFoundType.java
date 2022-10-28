package com.swygbro.housemate.exception.datanotfound;

public enum DataNotFoundType {

    DEFAULT("필수"),
    멤버를_찾을_수_없습니다("멤버를 찾을 수 없습니다."),
    그룹을_찾을_수_없습니다("그룹을 찾을 수 없습니다."),
    반복_주기를_찾을_수_없습니다("반복 주기를 찾을 수 없습니다."),
    집안일을_찾을_수_없습니다("집안일을 찾을 수 없습니다."),
    편지를_찾을_수_없습니다("편지를 찾을 수 없습니다."),
    편지의_내용을_찾을_수_없습니다("편지의 내용을 찾을 수 없습니다."),
    TableLock_을_찾을_수_없습니다("TableLock을 찾을 수 없습니다."),
    ;

    private final String message;

    DataNotFoundType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}