package exceptions;

public class SysYException extends Exception {

    public enum ExceptionCode {
        a, b, c, d, e, f, g, h, i, j, k, l, m;
    }

    public ExceptionCode exceptionCode;

    public SysYException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    @Override
    public String toString() {
        return exceptionCode.name();
    }
}
