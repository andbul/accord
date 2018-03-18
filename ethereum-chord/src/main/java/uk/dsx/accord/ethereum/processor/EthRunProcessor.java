package uk.dsx.accord.ethereum.processor;

import uk.dsx.accord.common.InstanceProcessor;
import uk.dsx.accord.ethereum.EthInstance;
import uk.dsx.accord.ethereum.EthInstanceContainer;
import uk.dsx.accord.ethereum.EthNode;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EthRunProcessor implements InstanceProcessor<EthInstanceContainer> {

    @Override
    public void process(EthInstanceContainer container) {
        List<EthInstance> instances = container.getInstances();


        instances.forEach(instance -> instance
                .run()
                .prepare()
                .exec());

        //Start bootNode
        EthNode bootNode = instances.stream()
                .map(EthInstance::getNodes)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .findFirst().orElseThrow(() -> new RuntimeException("aaa"));

        // Run bootNode
        bootNode.run("");
        String bootEnode = bootNode.getEnode();

        // Run node
        instances.stream()
                .map(EthInstance::getNodes)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .forEach(ethNode -> ethNode.run(bootEnode));
    }

}