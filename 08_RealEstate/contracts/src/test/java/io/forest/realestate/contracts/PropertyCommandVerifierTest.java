package io.forest.realestate.contracts;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import io.forest.realestate.action.ActionApprovedByBank;
import io.forest.realestate.action.ActionApprovedBySurveyor;
import io.forest.realestate.action.ActionCreate;
import io.forest.realestate.action.ActionTransferOwner;
import net.corda.core.contracts.CommandWithParties;

class PropertyCommandVerifierTest {


	@TestFactory
	Stream<DynamicTest> testInstance_withPropertyCommands_expectSucess() {
		List<PropertyCommands> inputList = Arrays.asList(
			new PropertyCommands.Create(),
			new PropertyCommands.Transfer(),
			new PropertyCommands.BankApproval(),
			new PropertyCommands.SurveyorApproval()
		);
		
		List<Class> outputList = Arrays.asList(
				ActionCreate.class,
				ActionTransferOwner.class,
				ActionApprovedByBank.class,
				ActionApprovedBySurveyor.class
			);
		
		return inputList.stream().map(
			i -> dynamicTest(
				String.format("Test %s", i.getClass().getSimpleName()), 
				() -> {
					CommandWithParties<PropertyCommands> commandType = mock(CommandWithParties.class);
					when(commandType.getValue()).thenReturn(i);
					
					int idx = inputList.indexOf(i);
					outputList.get(idx);
					
					assertThat(PropertyCommandVerifier.instance(commandType),
							instanceOf(outputList.get(idx)));
				}
			)
		);
	}

}
