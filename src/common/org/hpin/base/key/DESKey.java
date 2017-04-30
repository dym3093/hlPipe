package org.hpin.base.key;

public class DESKey {


	     private static String key="LONGGE36";
		// TODO Auto-generated method stub
		public static void main(String[] args) throws Exception {
	        String value = "中华人民共和国";
	        byte[] bytes = addKeys(value);
	        
	        String vv = "7E4E1BCAD8EDC23EDB3F9F6E411B05C05A81CF6208554649EA02B2AEE04018685AC95539F5C19A698B15AFFEC3BC852148FF4920AF30E1B73303A50355C32FB06D20E631C2F6627A90AEEC8BCCA0FFCF5F64C9309320B67B5E7762784E2F9719E9B6DCF0EF724165";
	        String str = rKeys(bytes);
	        
	    }
		
		 public static byte[] addKeys(String str){
			 DESEncrypt desEncrypt = new DESEncrypt(key.getBytes());
		        byte[] encryptText = null;
				try {
					encryptText = desEncrypt.doEncrypt(str.getBytes());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        return encryptText;
		 }
		 
		 public static String rKeys(byte[] str){
			 DESDecrypt desDecrypt = new DESDecrypt(key.getBytes());
		        byte[] decryptText = null;
				try {
					decryptText = desDecrypt.doDecrypt(str);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        String strs = new String(decryptText);
		        System.out.println("doDecryp=" +strs );
		        return strs;
		 }
		
	    /**
	     * 16进制显示数据
	     *
	     * @param value 字节数组
	     * @return
	     */
	    public static String toHexString(byte[] value) {
	        String newString = "";
	        for (int i = 0; i < value.length; i++) {
	            byte b = value[i];
	            String str = Integer.toHexString(b);
	            if (str.length() > 2) {
	                str = str.substring(str.length() - 2);
	            }
	            if (str.length() < 2) {
	                str = "0" + str;
	            }
	            newString += str;
	        }
	        return newString.toUpperCase();
	    }
	

}
