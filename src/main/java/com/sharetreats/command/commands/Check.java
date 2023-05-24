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

public class Check extends AbstractCommand {

    private static final Check instance;

    public static Check getInstance() {
        return instance;
    }

    static {
        MockupData mockupData = MockupDataImpl.getInstance();
        CodeGeneratorConfigGetter codeGeneratorConfigGetter = (CodeGeneratorConfigGetter) mockupData;
        VoucherService voucherService1 = VoucherServiceImpl.getInstance();
        instance = new Check(codeGeneratorConfigGetter, voucherService1);
    }

    private Check(CodeGeneratorConfigGetter codeGeneratorConfigGetter, VoucherService voucherService) {
        super(codeGeneratorConfigGetter, voucherService);
    }

    @Override
    public String process(String command) {
        String code = parse(command)[0];
        return voucherService.check(code);
    }

    @Override
    public String[] parse(String command) {

        String[] segments = segment(command);

        validate(segments);

        String itemCode = createValidatedItemCodeFrom(Arrays.copyOfRange(segments, 1, segments.length));

        return new String[]{itemCode};
    }

    private void validate(String[] segments) {
        if (segments.length != 4) {
            throw new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MALFORMED_CODE);
        }
    }
}
