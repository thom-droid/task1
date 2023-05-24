package com.sharetreats.command;

import com.sharetreats.command.commands.Check;
import com.sharetreats.test_utils.CommandTestUtils;
import com.sharetreats.test_utils.TestMockData;
import com.sharetreats.voucher.Voucher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandEnumTest {

    CommandEnumWrapperFactory commandEnumWrapperFactory = new CommandEnumWrapperFactoryImpl(CommandTestUtils.MockEnumCommand.class);
    CommandEnumWrapper commandWrapper = commandEnumWrapperFactory.createCommandEnumWrapper();
    CommandController commandController = new CommandController(commandWrapper);
    TestMockData mockupData = (TestMockData) CommandTestUtils.MockCommandInitiator.getMockupData();

    @Test
    public void givenCheckCommand_whenProcessInvoked_thenDoesNotThrow() {

        String itemCode = mockupData.getAnyCode();
        String input = "check " + itemCode;

        assertDoesNotThrow(() -> commandController.process(input));

    }

    @Test
    public void givenClaimCommand_whenProcessInvoked_thenDoesNotThrow() {

        Voucher voucher = mockupData.getRandomAvailableVoucher();
        String shopCode = voucher.getItem().getShop().getCode();
        String itemCode = voucher.getCode();

        String input = "claim " + shopCode + " " + itemCode;

        assertDoesNotThrow(() -> commandController.process(input));
    }

    @Test
    public void givenCommandName_thenFindTheSameInstance() {

        String command = "CHECK";

        Command command1 = CommandEnum.CHECK.findCommand(command);
        Command command2 = Check.getInstance();

        assertEquals(command1, command2);

    }
}