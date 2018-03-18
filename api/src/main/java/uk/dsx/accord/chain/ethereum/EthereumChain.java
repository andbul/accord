package uk.dsx.accord.chain.ethereum;

import com.mashape.unirest.http.exceptions.UnirestException;
import uk.dsx.accord.block.EthereumBlock;
import uk.dsx.accord.chain.BasicChain;
import uk.dsx.accord.peer.EthereumPeerInfo;
import uk.dsx.accord.transaction.EthereumTransaction;
import uk.dsx.accord.util.HttpHelper;

public class EthereumChain implements BasicChain<EthereumPeerInfo, EthereumBlock, EthereumTransaction> {

    String url;
    int port;

    @Override
    public Iterable<EthereumPeerInfo> getPeers() throws UnirestException {
        return HttpHelper.<Iterable<EthereumPeerInfo>>post(url+port, "admin_peers");

    }

    @Override
    public EthereumBlock getBlock(String hash) throws UnirestException {
        return HttpHelper.<EthereumBlock, Object[]>post(url+port, "eth_getBlockByNumber", new Object[] {hash, "pending", true});
    }

    public String sendTransaction(EthereumTransaction transaction) throws UnirestException {
        return HttpHelper.<String, Object[]>post(url+port, "eth_sendTransaction", new Object[] {transaction});
    }

    public int getPeerCount() throws UnirestException {
        String result = HttpHelper.<String>post(url+port, "net_peerCount");
        return Integer.parseInt(result.substring(2));
    }

    public double getBalance(String address ) throws UnirestException {
        String weiBalance = HttpHelper.<String, Object[]>post(url+port, "eth_getBalance", new Object[] {address, "pending"});
        //TODO use converter, this is not worked
        return Double.parseDouble(weiBalance);
    }

    public int getTransactionCount(String address) throws UnirestException {
        String result = HttpHelper.<String, Object[]>post(url+port, "eth_getTransactionCount", new Object[] {address, "pending"});
        return Integer.parseInt(result.substring(2));
    }
}