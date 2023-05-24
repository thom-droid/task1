package com.sharetreats.command.commands;

import com.sharetreats.code.CodeGeneratorConfigGetter;
import com.sharetreats.command.AbstractCommand;
import com.sharetreats.mock.MockupData;
import com.sharetreats.mock.MockupDataImpl;
import com.sharetreats.voucher.VoucherService;
import com.sharetreats.voucher.VoucherServiceImpl;

public class Exit extends AbstractCommand {

    private static final Exit instance;

    public static Exit getInstance() {
        return instance;
    }

    static {
        MockupData mockupData = MockupDataImpl.getInstance();
        CodeGeneratorConfigGetter codeGeneratorConfigGetter1 = (CodeGeneratorConfigGetter) mockupData;
        VoucherService voucherService1 = VoucherServiceImpl.getInstance();
        instance = new Exit(codeGeneratorConfigGetter1, voucherService1);
    }

    private Exit(CodeGeneratorConfigGetter codeGeneratorConfigGetter, VoucherService voucherService) {
        super(codeGeneratorConfigGetter, voucherService);
    }

    @Override
    public String process(String command) {
        return "EXIT";
    }

    @Override
    public String[] parse(String command) {
        return null;
    }
}
