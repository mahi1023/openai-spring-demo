package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import com.example.demo.text.prompttemplate.records.CountryCuisines;

@Service
public class OpenAiService {
    
    private final ChatClient chatClient;  // Fixed typo: was chatCilent
   
    @Autowired
    private EmbeddingModel embeddingModel ;
    
    public OpenAiService(ChatClient.Builder builder, ChatMemory chatMemory) {
//    	chatClient = builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//    			.build();
    	
        this.chatClient = builder.build();

    }
    
    public ChatResponse generateAnswer(String question,String conversationId) {	
//    	OpenAiChatOptions options = OpenAiChatOptions.builder()
//    			.model("gpt-3.5-turbo")
//    			.temperature(0.7)
//    			.maxTokens(20)
//    			.build();
    	
    	return chatClient.prompt(question)
                .advisors(advisor -> advisor.param("conversationId", conversationId))
                .call()
                .chatResponse();
    }
    
    public String getTravelGuidence(String city, String month, String language, String budget) {
		PromptTemplate template = new PromptTemplate("Welcome to the {city} travel guide!\n"
				+ "If you're visiting in {month}, here's what you can do:\n"
				+ "1. Must-visit attractions.\n"
				+ "2. Local cuisine you must try.\n"
				+ "3. Useful phrases in {language}.\n"
				+ "4. Tips for traveling on a {budget} budget.\n"
				+ "Enjoy your trip!");
		Prompt prompt = template.create(Map.of("city",city,"month",month,"language",language,"budget",budget));
		
		return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    	
    }

	public CountryCuisines getCuisines(String country, String numCuisines, String language) {
		PromptTemplate template = new PromptTemplate("You are an expert in traditional cuisines.\n"
				+ "You provide information about a specific dish from a specific\n"
				+ "country.\n"
				+ "Answer the question: What is the traditional cuisine of {country}?\n"
				+ "Avoid giving information about fictional places. If the country is\n"
				+ "fictional\n"
				+ "or non-existent answer: I don't know.");
		Prompt prompt = template.create(Map.of("country",country,"numCuisines",numCuisines,"language",language));
		
		return chatClient.prompt(prompt).call().entity(CountryCuisines.class);
	}

	public ChatResponse genrateInterviewQuestion(String company, String jobTitle, String strength, String weakness) {
		
		PromptTemplate t = new PromptTemplate("You are a career coach. Provide tailored interview tips for the\n"
				+ "position at {jobTitle}  at {company}.\n"
				+ "Highlight your strengths in {strength} and prepare for questions\n"
				+ "about your weaknesses such as {weakness}.");
		Prompt p = t.create(Map.of("company",company,"jobTitle",jobTitle,"strength",strength,"weakness",weakness));
		
		return chatClient.prompt(p).call().chatResponse();
	}
	
	public float [] embed(String text) {
		return embeddingModel.embed(text);
	}
	
	public double findSimilarity(String text1, String text2) {
		List<float[]> response = embeddingModel.embed(List.of(text1,text2));
		return cosineSimilarity(response.get(0),response.get(1));
	}
	
	private double cosineSimilarity(float[] vectorA, float[] vectorB) {
		if (vectorA.length != vectorB.length) {
			throw new IllegalArgumentException("Vectors must be of the same length");
		}

		// Initialize variables for dot product and magnitudes
		double dotProduct = 0.0;
		double magnitudeA = 0.0;
		double magnitudeB = 0.0;

		// Calculate dot product and magnitudes
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			magnitudeA += vectorA[i] * vectorA[i];
			magnitudeB += vectorB[i] * vectorB[i];
		}

		// Calculate and return cosine similarity
		return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
	}
}