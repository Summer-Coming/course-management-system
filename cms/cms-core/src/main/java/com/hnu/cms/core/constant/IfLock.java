package com.stylefeng.guns.core.constant;

public enum IfLock {
        YES(1, "是"),
        NO(0, "不是");

        int code;
        String message;

        IfLock(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static String valueOf(Integer status) {
            if (status == null) {
                return "";
            } else {
                for (IfLock s : IfLock.values()) {
                    if (s.getCode() == status) {
                        return s.getMessage();
                    }
                }
                return "";
            }
        }

}
