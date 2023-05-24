package com.sharetreats.command;

import com.sharetreats.code.CodeGeneratorConfigGetter;
import com.sharetreats.code.CodeGeneratorConfigure;
import com.sharetreats.exception.CustomRuntimeException;
import com.sharetreats.exception.CustomRuntimeExceptionCode;
import com.sharetreats.voucher.VoucherService;

import java.util.Objects;
import java.util.regex.Pattern;

/** 각 명령어가 입력받은 내용을 검증할 수 있도록 공통 메서드를 정의한 추상 클래스입니다. */

public abstract class AbstractCommand implements Command {

    private static final Pattern NUMERIC_REGEX = Pattern.compile("\\d+");
    private static final Pattern ALPHABET_LOWERCASE_REGEX = Pattern.compile("[a-z]+");
    private static final Pattern ALPHABET_UPPERCASE_REGEX = Pattern.compile("[A-Z]+");
    private static final Pattern ALPHABET_REGEX = Pattern.compile("[A-Za-z]+");

    protected final int itemCodeLength;
    protected final int shopCodeLength;
    protected final String itemCodeCharset;
    protected final String shopCodeCharset;
    protected final VoucherService voucherService;
    private final CodeGeneratorConfigGetter codeGeneratorConfigGetter;

    protected AbstractCommand(CodeGeneratorConfigGetter codeGeneratorConfigGetter, VoucherService voucherService) {
        this.codeGeneratorConfigGetter = codeGeneratorConfigGetter;
        this.itemCodeLength = codeGeneratorConfigGetter.getItemCodeConfig().getLength();
        this.itemCodeCharset = codeGeneratorConfigGetter.getItemCodeConfig().getCharset();
        this.shopCodeLength = codeGeneratorConfigGetter.getShopCodeConfig().getLength();
        this.shopCodeCharset = codeGeneratorConfigGetter.getShopCodeConfig().getCharset();
        this.voucherService = voucherService;
    }

    protected abstract String[] parse(String command);

    // 입력된 인풋에 대해 null 체크 및 빈 문자열인지 확인하고, 정의된 문자열을 쪼갭니다.
    protected String[] segment(String input) {
        Objects.requireNonNull(input);

        if (input.isBlank()) {
            throw new CustomRuntimeException(CustomRuntimeExceptionCode.EMPTY_COMMAND);
        }

        input = input.trim();
        return input.split(" ");
    }

    // 상점 코드르를 검증합니다. 설정한 코드의 길이와 일치하는지, 띄어쓰기는 제대로 되었는지, 설정한 charset 과 일치하는지 검증합니다.
    // 검증에 성공하면 실행에 알맞는 형태의 문자열로 리턴합니다
    protected String createValidatedItemCodeFrom(String[] codeSegment) {
        Pattern regex = createRegex(itemCodeCharset);
        int len = 0;
        int part = codeGeneratorConfigGetter.getItemCodeConfig().getSpace();
        int partLength = itemCodeLength / part;

        for (String s : codeSegment) {

            if (!regex.matcher(s).matches()) {
                throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
            }

            int length = s.length();

            if (length != partLength) {
                throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
            }

            len += length;
        }

        if (len != itemCodeLength) {
            throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
        }

        return String.join(" ", codeSegment[0], codeSegment[1], codeSegment[2]);
    }

    // 코드 설정에 따라 코드를 검색할 정규식을 세팅합니다
    protected Pattern createRegex(String charset) {
        if (charset.equals(CodeGeneratorConfigure.Charset.NUMERIC)) {
            return NUMERIC_REGEX;
        }

        if (charset.equals(CodeGeneratorConfigure.Charset.ALPHABET_LOWERCASE)) {
            return ALPHABET_LOWERCASE_REGEX;
        }

        if (charset.equals(CodeGeneratorConfigure.Charset.ALPHABET_UPPERCASE)) {
            return ALPHABET_UPPERCASE_REGEX;
        }

        if (charset.equals(CodeGeneratorConfigure.Charset.ALPHABET)) {
            return ALPHABET_REGEX;
        }

        return NUMERIC_REGEX;
    }

}
