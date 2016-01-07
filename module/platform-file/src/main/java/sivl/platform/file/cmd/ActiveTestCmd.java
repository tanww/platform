package sivl.platform.file.cmd;import java.io.IOException;import java.net.Socket;import sivl.platform.file.model.FileResultModel;public class ActiveTestCmd extends AbstractCmd<Boolean> {	public ActiveTestCmd() {		super();		this.requestCmd = FDFS_PROTO_CMD_ACTIVE_TEST;	}	@Override	public FileResultModel<Boolean> exec(Socket socket) throws IOException {		request(socket.getOutputStream());        Response response = response(socket.getInputStream());        if(response.isSuccess()) {            return new FileResultModel<Boolean>(SUCCESS_CODE,true);        }else {            return new FileResultModel<Boolean>(response.getCode(),false);        }	}}