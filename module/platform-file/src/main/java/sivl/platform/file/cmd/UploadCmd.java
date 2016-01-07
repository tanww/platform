package sivl.platform.file.cmd;import java.io.File;import java.io.IOException;import java.net.Socket;import java.util.Arrays;import sivl.platform.file.model.FileResultModel;import sivl.platform.file.utils.BityUtils;import sivl.platform.file.utils.FileUtils;/** * 上传命令 * 目的： * @author lixuefeng * @date 2016年1月7日 * @version 1.0 */public class UploadCmd extends AbstractCmd<String> {		private File file;	private byte[] fileByte;	/**    * 构造命令    * @param file    * @param extName 文件扩展名，如果传入一个完整文件名，将截取扩展名    * @param storePathIndex    */	public UploadCmd(byte[] fileByte,String extName,byte storePathIndex){		super();		this.fileByte = fileByte;		this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_FILE;		this.body2Len = fileByte.length;		this.responseCmd = STORAGE_PROTO_CMD_RESP;		this.responseSize = -1;		this.body = new byte[15];		Arrays.fill(body, (byte) 0);		this.body[0] = storePathIndex;		byte[] fileSizeByte = BityUtils.long2buff(body2Len);		byte[] fileExtNameByte = FileUtils.getFileExtNameByte(this,extName);		int fileExtNameByteLen = fileExtNameByte.length;		if(fileExtNameByteLen>FDFS_FILE_EXT_NAME_MAX_LEN){			fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;		}		System.arraycopy(fileSizeByte, 0, body, 1, fileSizeByte.length);		System.arraycopy(fileExtNameByte, 0, body, fileSizeByte.length + 1, fileExtNameByteLen);	}    /**     * 构造命令     * @param file     * @param extName 文件扩展名，如果传入一个完整文件名，将截取扩展名     * @param storePathIndex     */	public UploadCmd(File file,String extName,byte storePathIndex){		super();		this.file = file;		this.requestCmd = STORAGE_PROTO_CMD_UPLOAD_FILE;		this.body2Len = file.length();		this.responseCmd = STORAGE_PROTO_CMD_RESP;		this.responseSize = -1;		this.body = new byte[15];		Arrays.fill(body, (byte) 0);		this.body[0] = storePathIndex;		byte[] fileSizeByte = BityUtils.long2buff(file.length());		byte[] fileExtNameByte = FileUtils.getFileExtNameByte(this,extName);		int fileExtNameByteLen = fileExtNameByte.length;		if(fileExtNameByteLen>FDFS_FILE_EXT_NAME_MAX_LEN){			fileExtNameByteLen = FDFS_FILE_EXT_NAME_MAX_LEN;		}		System.arraycopy(fileSizeByte, 0, body, 1, fileSizeByte.length);		System.arraycopy(fileExtNameByte, 0, body, fileSizeByte.length + 1, fileExtNameByteLen);	}		@Override	public FileResultModel<String> exec(Socket socket) throws IOException {		if(file!=null){			fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(file);		}		request(socket.getOutputStream(), fileByte);		Response response = response(socket.getInputStream());		if(response.isSuccess()){			byte[] data = response.getData();			String group = new String(data, 0,	FDFS_GROUP_NAME_MAX_LEN).trim();			String remoteFileName = new String(data,FDFS_GROUP_NAME_MAX_LEN, data.length - FDFS_GROUP_NAME_MAX_LEN);			FileResultModel<String> result = new FileResultModel<String>(response.getCode());			result.setData(group + "/" + remoteFileName);			return result;		}else{			FileResultModel<String> result = new FileResultModel<String>(response.getCode());			result.setMessage("Error");			return result;		}	}//	private byte[] getFileExtNameByte(String fileName) {//		String fileExtName = null;//		int nPos = fileName.lastIndexOf('.');//		if (nPos > 0 && fileName.length() - nPos <= FDFS_FILE_EXT_NAME_MAX_LEN + 1) {//			fileExtName = fileName.substring(nPos + 1);//            if (fileExtName != null && fileExtName.length() > 0) {//                return fileExtName.getBytes(charset);//            }else{//                return new byte[0];//            }//		} else {//            //传入的即为扩展名//            return fileName.getBytes(charset);//        }//	}}