package mejai.mejaigg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mejai.mejaigg.dto.riot.SummonerDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.response.UserProfileDto;
import mejai.mejaigg.repository.UserRepository;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PropertySource(ignoreResourceNotFound = false, value = "classpath:riotApiKey.properties")
public class UserService {
	private ObjectMapper objectMapper = new ObjectMapper();

	private final UserRepository userRepository;
	@Value("${riot.api.key}")
	private String mykey;

	public SummonerDTO summonerTest(String name){
		SummonerDTO result;
		String serverUrl = "https://kr.api.riotgames.com";
		//이름만 들어온 경우에는 다음과 같이 요청 가능
		try{
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + mykey);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != 200)
				return null;
			result = objectMapper.readValue(response.getEntity().getContent(), SummonerDTO.class);
		}
		catch (IOException e){
			e.printStackTrace();
			return null;
		}
		return result;
	}

	@Transactional
	public UserProfileDto getUserProfile(String name) {
		//이전에 유저가 가입한 적 있는 경우
//		  if (user == null) {
//
//		  } else {
//			  //유저가 가입한 적 없는 경우
//			  //라이엇 API에 요청
//			  //유저 정보 저장
//			  //유저 정보 반환
//
//		  }
		return null;
		// }
	}
}
