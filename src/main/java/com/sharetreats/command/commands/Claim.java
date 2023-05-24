package com.sharetreats.command.commands;

import com.sharetreats.code.CodeGeneratorConfigGetter;
import com.sharetreats.command.AbstractCommand;
import com.sharetreats.exception.CustomRuntimeException;
import com.sharetreats.exception.CustomRuntimeExceptionCode;
import com.sharetreats.mock.MockupData;
import com.sharetreats.mock.MockupDataImpl;
import com.sharetreats.voucher.VoucherService;
import com.sharetreats.voucher.VoucherServiceImpl;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Claim extends AbstractCommand {

    private static final Claim instance;

    static {
        MockupData mockupData = MockupDataImpl.getInstance();
        CodeGeneratorConfigGetter codeGeneratorConfigGetter1 = (CodeGeneratorConfigGetter) mockupData;
        VoucherService voucherService1 = VoucherServiceImpl.getInstance();
        instance = new Claim(codeGeneratorConfigGetter1, voucherService1);
    }

    public static Claim getInstance() {
        return instance;
    }

    private Claim(CodeGeneratorConfigGetter codeGeneratorConfigGetter, VoucherService voucherService) {
        super(codeGeneratorConfigGetter, voucherService);
    }

    @Override
    public String process(String command) {
        String[] codes = parse(command);
        String shopCode = codes[0];
        String itemCode = codes[1];
        return voucherService.claim(shopCode, itemCode);
    }

    @Override
    public String[] parse(String command) {

        String[] segments = segment(command);

        validate(segments);

        String itemCode = createValidatedItemCodeFrom(Arrays.copyOfRange(segments, 2, segments.length));
        String shopCode = segments[1];

        return new String[]{shopCode, itemCode};
    }

    private void validate(String[] segments) {
        validateSegments(segments);
        String shopCode = segments[1];
        validateShopCode(shopCode);
    }

    private void validateSegments(String[] segments) {
        if (segments.length != 5)
            throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
    }

    private void validateShopCode(String shopCode) {
        Pattern regex = createRegex(shopCodeCharset);
        if (shopCode.length() != shopCodeLength || !regex.matcher(shopCode).matches()) {
            throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
        }
    }
}
