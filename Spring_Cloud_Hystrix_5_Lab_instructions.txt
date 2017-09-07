Make the following changes to service-sentence application.

1)Write WordService and WordServiceImpl to wrap calls to feign clients like below.

	public interface WordService {
		String getSubject();
		String getVerb();
		String getArticle();
		String getAdjective();
		String getNoun();
	}
	
	@Service
	public class WordServiceImpl implements WordService{

		@Autowired VerbClient verbClient;
		@Autowired SubjectClient subjectClient;
		@Autowired ArticleClient articleClient;
		@Autowired AdjectiveClient adjectiveClient;
		@Autowired NounClient nounClient;
		
		@Override
		public String getSubject() {
			return subjectClient.getWord();
		}
		
		@Override
		public String getVerb() {
			return verbClient.getWord();
		}
		
		@Override
		public String getArticle() {
			return articleClient.getWord();
		}
		
		@Override
		public String getAdjective() {
			return adjectiveClient.getWord();
		}
		
		@Override
		public String getNoun() {
			return nounClient.getWord();
		}
	}

2)Modify SentenceServiceImpl like below.
	
	@Service
	public class SentenceServiceImpl implements SentenceService {

		@Autowired WordService wordService;

		/**
		 * Assemble a sentence by gathering random words of each part of speech:
		 */
		public String buildSentence() {
			return  
				String.format("%s %s %s %s %s.",
					wordService.getSubject(),
					wordService.getVerb(),
					wordService.getArticle(),
					wordService.getAdjective(),
					wordService.getNoun())
				;
		}
	}

3)Add the maven dependency 	"spring-cloud-starter-hystrix" to pom.xml.

4)Edit the main Application configuration class and add @EnableHystix.

5)Refactor the WordServiceImpl to use Hystrix. We have decided that it is not strictly necessary to have an adjective in our sentence if the adjective service is failing, so modify the getAdjective service to run within a Hystrix Command. Establish a fallback method that will return an empty String ("").

6)Stop any previously running sentence server and launch your new one. Test it to make sure it works by opening http://localhost:8020/sentence. The application should work the same as it did before, though the “Adjective” call is now going through a Hystrix circuit breaker.

7)Locate and STOP the Adjective service. Refresh http://localhost:8020/sentence. The sentence should appear without an adjective. Restart the adjective service. Once the Eureka registration is complete and the circuit breaker re-closes, the sentence server will once again display adjectives.

