package com.sharetreats.test_utils;

import com.sharetreats.code.CodeGeneratorConfigGetter;
import com.sharetreats.command.Command;
import com.sharetreats.command.CommandEnumWrapper;
import com.sharetreats.command.commands.Check;
import com.sharetreats.command.commands.Claim;
import com.sharetreats.command.commands.Exit;
import com.sharetreats.command.commands.Help;
import com.sharetreats.exception.CustomRuntimeException;
import com.sharetreats.exception.CustomRuntimeExceptionCode;
import com.sharetreats.mock.MockupData;
import com.sharetreats.voucher.VoucherService;
import com.sharetreats.voucher.VoucherServiceImpl;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

public class CommandTestUtils {

    public enum MockEnumCommand implements CommandEnumWrapper {

        CHECK(() -> {
            try {
                return MockCommandInitiator.getCheck();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }),

        CLAIM(() -> {
            try {
                return MockCommandInitiator.getClaim();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }),

        HELP(() -> {
            try {
                return MockCommandInitiator.getHelp();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }),

        EXIT(() -> {
            try {
                return MockCommandInitiator.getExit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }),

        ;

        final Supplier<Command> supplier;
        final Command command;

        MockEnumCommand(Supplier<Command> supplier) {
            this.supplier = supplier;
            this.command = supplier.get();
        }

        @Override
        public Command findCommand(String command) {
            MockEnumCommand enumCommand = Arrays.stream(this.getClass().getEnumConstants())
                    .filter(e -> e.name().equals(command.toUpperCase(Locale.ROOT)))
                    .findFirst()
                    .orElseThrow(() -> new CustomRuntimeException(CustomRuntimeExceptionCode.ILLEGAL_ARGUMENT_MISTYPED_COMMAND));

            return enumCommand.getCommand();
        }

        public Command getCommand() {
            return command;
        }
    }

    public static class MockCommandInitiator {

        private static final MockupData MOCKUP_DATA = new TestMockData();
        private static final CodeGeneratorConfigGetter CODE_GENERATOR_CONFIG_GETTER = (CodeGeneratorConfigGetter) MOCKUP_DATA;
        private static final VoucherService VOUCHER_SERVICE = VoucherServiceImpl.getInstance(MOCKUP_DATA);

        public static MockupData getMockupData() {
            return MOCKUP_DATA;
        }

        public static VoucherService getVoucherService() {
            return VOUCHER_SERVICE;
        }

        public static Check getCheck() throws Exception {
            Constructor<Check> constructor = Check.class.getDeclaredConstructor(CodeGeneratorConfigGetter.class, VoucherService.class);
            constructor.setAccessible(true);

            return constructor.newInstance(CODE_GENERATOR_CONFIG_GETTER, VOUCHER_SERVICE);
        }

        public static Claim getClaim() throws Exception {
            Constructor<Claim> constructor = Claim.class.getDeclaredConstructor(CodeGeneratorConfigGetter.class, VoucherService.class);
            constructor.setAccessible(true);

            return constructor.newInstance(CODE_GENERATOR_CONFIG_GETTER, VOUCHER_SERVICE);
        }

        public static Help getHelp() throws Exception {
            Constructor<Help> constructor = Help.class.getDeclaredConstructor(CodeGeneratorConfigGetter.class, VoucherService.class);
            constructor.setAccessible(true);

            return constructor.newInstance(CODE_GENERATOR_CONFIG_GETTER, VOUCHER_SERVICE);
        }

        public static Exit getExit() throws Exception {
            Constructor<Exit> constructor = Exit.class.getDeclaredConstructor(CodeGeneratorConfigGetter.class, VoucherService.class);
            constructor.setAccessible(true);

            return constructor.newInstance(CODE_GENERATOR_CONFIG_GETTER, VOUCHER_SERVICE);
        }
    }
}
