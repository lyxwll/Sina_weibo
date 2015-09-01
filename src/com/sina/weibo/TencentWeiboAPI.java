package com.sina.weibo;

import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 腾讯微博API，可以根据自己的需要添加API
 * 
 * @author cstdingran@gmail.com
 * 
 */
public class TencentWeiboAPI {

	/**
	 * 访问微博服务接口的地址
	 */
	public static final String API_SERVER = "https://open.t.qq.com/api";

	private static final String URL_ADD = API_SERVER + "/t/add";

	/**
	 * 发表一条带图片的微博
	 * 
	 * http://open.t.qq.com/api/t/add_pic https://open.t.qq.com/api/t/add_pic
	 * OAuth2.0使用)
	 * 
	 * pic_url ：图片的URL地址，支持多张图片，此时各图片url之间用逗　号","隔开，且必须是微博域名下的url，
	 * 即调用upload_pic或add_pic接口获取到的图片url
	 * ，否则无效（最多支持9个url，多余9个忽略不计，单个URL最长不能超过1024字节） 这个图片的链接必须是微博域名下的，要先调用
	 * upload_pic 这个接口获取到的链接才可以。
	 */
	private static final String URL_ADD_PIC = API_SERVER + "t/add_pic";
	private static final String URL_UPLOAD_PIC = API_SERVER + "t/upload_pic";

	private static final String URL_USER_INFO = API_SERVER + "/user/info";

	/**
	 * post请求方式
	 */
	public static final String HTTPMETHOD_POST = "POST";

	/**
	 * get请求方式
	 */
	public static final String HTTPMETHOD_GET = "GET";

	private TencentTO tencentTO;

	/**
	 * 构造函数
	 */
	public TencentWeiboAPI(TencentTO tencentTO) {

		this.tencentTO = tencentTO;

	}

	/**
	 * 执行请求-通用参数
	 * 
	 * @param url
	 * @param params
	 * @param httpMethod
	 * @param listener
	 */
	private void request(final String url, final WeiboParameters params, final String httpMethod,
			RequestListener listener) {

		params.add(Constants.TX_API_APP_KEY, tencentTO.getAppkey());
		params.add(Constants.TX_API_ACCESS_TOKEN, tencentTO.getAccessToken());
		params.add(Constants.TX_API_OPEN_ID, tencentTO.getOpenId());
		params.add(Constants.TX_API_CLIENT_IP, tencentTO.getClientIp());
		params.add(Constants.TX_API_OAUTH_VERSION, "2.a");
		params.add(Constants.TX_API_SCOPE, "all");
		params.add(Constants.TX_API_FORMAT, "json"); // 返回数据的格式（json或xml）
		// AsyncWeiboRunner.request(url, params, httpMethod, listener);

	}

	/**
	 * 发送一条微博
	 * 
	 * @param content
	 *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
	 * @param longitude
	 *            经度，为实数，如113.421234（最多支持10位有效数字，可以填空）不是必填
	 * @param latitude
	 *            纬度，为实数，如22.354231（最多支持10位有效数字，可以填空） 不是必填
	 * @param syncflag
	 *            微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式 不是必填
	 * @param compatibleflag
	 *            容错标志，支持按位操作，默认为0。 0x20-微博内容长度超过140字则报错 0-以上错误做容错处理，即发表普通微博
	 *            不是必填
	 * @param listener
	 *            回调函数
	 */
	public void addWeibo(String content, long longitude, long latitude, int syncflag,
			int compatibleflag, RequestListener listener) {

		WeiboParameters params = new WeiboParameters(content);
		params.add(Constants.TX_API_CONTENT, content);
		params.add(Constants.TX_API_LONGITUDE, longitude);
		params.add(Constants.TX_API_LATITUDE, latitude);
		params.add(Constants.TX_API_SYNCFLAG, syncflag);
		params.add(Constants.TX_API_COMPATIBLEFLAG, compatibleflag);
		request(URL_ADD, params, HTTPMETHOD_POST, listener);

	}

	/**
	 * 
	 * @param pic_url
	 *            上传图片的URL地址，pic_type=1时使用（URL最长不能超过1024字节,图片大小最大不能超过4M）
	 * @param pic
	 *            文件域表单名,pic_type=2时使用。本字段不要放在签名的参数中，不然请求时会出现签名错误，图片大小限制在4M。(
	 *            重要提示pic_url与pic不能同时为空，最终的请求由pic_type决定，默认请求图片链接)
	 * @param pic_type
	 *            上传图片的类型，1：链接，2：本地图片
	 * @param listener
	 */
	public void uploadPic(String content, String pic_url, String pic, int pic_type,
			RequestListener listener) {

		WeiboParameters params = new WeiboParameters(content);
		params.add("content", content);
		params.add("pic_url", pic_url);
		params.add("pic", pic);
		params.add("pic_type", pic_type);
		request(URL_UPLOAD_PIC, params, HTTPMETHOD_POST, listener);

	}

	/**
	 * 
	 * @param content
	 * @param clientip
	 * @param longitude
	 * @param latitude
	 * @param pic
	 * @param syncflag
	 * @param compatibleflag
	 * @param empty
	 * @param listener
	 */
	public void addPic(String content, long longitude, long latitude, String pic, int syncflag,
			int compatibleflag, int empty, RequestListener listener) {

		WeiboParameters params = new WeiboParameters(content);
		params.add("content", content);
		params.add("longitude", longitude);
		params.add("latitude", latitude);
		params.add("pic", pic);
		params.add("syncflag", syncflag);
		params.add("compatibleflag", compatibleflag);
		params.add("empty", empty);
		request(URL_ADD_PIC, params, HTTPMETHOD_POST, listener);

	}

	public void addPic(String content, long longitude, long latitude, int syncflag,
			int compatibleflag, RequestListener listener) {

		WeiboParameters params = new WeiboParameters(content);
		params.add(Constants.TX_API_CONTENT, content);
		params.add(Constants.TX_API_LONGITUDE, longitude);
		params.add(Constants.TX_API_LATITUDE, latitude);
		params.add(Constants.TX_API_SYNCFLAG, syncflag);
		params.add(Constants.TX_API_COMPATIBLEFLAG, compatibleflag);
		request(URL_ADD_PIC, params, HTTPMETHOD_POST, listener);

	}

	/**
	 * OAuth授权之后，获取授权用户的信息
	 * 
	 * @param listener
	 */
	public void getUserInfo(RequestListener listener) {

		WeiboParameters params = new WeiboParameters(null);
		request(URL_USER_INFO, params, HTTPMETHOD_GET, listener);

	}

}