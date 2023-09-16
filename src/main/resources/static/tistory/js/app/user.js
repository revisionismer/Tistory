/**
 *  user.js
 */

$(document).ready(function(){

	var ACCESS_TOKEN = getCookie('access_token');
		
	function getCookie(key) {
		let result = null;
		let cookie = document.cookie.split(';');
		cookie.some(function(item) {
			item = item.replace(' ', '');
				
			let dic = item.split('=');
			if(key === dic[0]) {
				result = dic[1];
				return true;
			}
		});
		return result;
	}
		
	console.log(ACCESS_TOKEN);
			
	if(ACCESS_TOKEN != null) {
		console.log(ACCESS_TOKEN);
				
		var header = ACCESS_TOKEN.split('.')[0];
		var payload = ACCESS_TOKEN.split('.')[1];
		var signature = ACCESS_TOKEN.split('.')[2];
				
		console.log(header);
		console.log(payload);
		console.log(signature);
				
		console.log("복호화 : " + Base64.decode(payload));
				
		// 2023-02-12 -> 토큰에 있는 payload에 실어논 정보를 Base64로 디코드해서 가져와 세팅(base64.min.js 필요)
		var data = JSON.parse(Base64.decode(payload));

		console.log(data.username);
	}
	
	
	/**
	 * 2-1. 회원 정보 조회 
	 */
	var userUpdateForm = $("#userUpdateForm").val();
	
	if(userUpdateForm != null) {
		$("#userInfo").ready(function(){
			
			if(ACCESS_TOKEN != null) {
				$.ajax({
					type : "GET",
					url : "/api/users/s/info",
					contentType : "application/json; charset=UTF-8",
					headers: {
						"Authorization" : "Bearer " + ACCESS_TOKEN
					},
					success : function(res) {
						console.log(res);	
						
						$("#userId").val(res.data.id);
						$("#username").val(res.data.username);
						$("#email").val(res.data.email);
							
					},
					error : function(res) {
						console.log(res);
						alert(res.responseJSON.message);
						location.href = "/login";
						return;
					}
				});	
				
			} else {
				alert("로그인을 해주세요.");
				location.href = "/login";
				return;
			}
			
		});
	}
	
});