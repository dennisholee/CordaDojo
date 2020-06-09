package io.forest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;

@RestController
public class ApiController {

	@RequestMapping(method=RequestMethod.POST, path = "/foo")
	public String foo()  {
		NetworkHostAndPort nodeAddress = new NetworkHostAndPort("localhost", 10006);
		
		final CordaRPCClient client = new CordaRPCClient(nodeAddress);
        final CordaRPCConnection connection = client.start("user1", "test");
        final CordaRPCOps cordaRPCOperations = connection.getProxy();

       // logger.info(cordaRPCOperations.currentNodeTime().toString());
        String nodeInfo = cordaRPCOperations.nodeInfo().toString();

        connection.notifyServerAndClose();
        
		return nodeInfo;
	}
}
