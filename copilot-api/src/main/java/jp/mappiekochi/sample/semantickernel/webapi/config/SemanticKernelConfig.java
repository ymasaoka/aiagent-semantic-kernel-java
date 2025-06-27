package jp.mappiekochi.sample.semantickernel.webapi.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.agents.chatcompletion.ChatCompletionAgent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SemanticKernelConfig {
    @Value("${azure.openai.api.key}")
    private String apiKey;
    @Value("${azure.openai.api.endpoint}")
    private String endpoint;
    @Value("${azure.openai.deployment.model.name}")
    private String deploymentModelName;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildAsyncClient();
    }

    @Bean
    public ChatCompletionService openAIChatCompletion(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(openAIAsyncClient)
                .withModelId(deploymentModelName)
                .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withFunctionChoiceBehavior(
                        com.microsoft.semantickernel.functionchoice.FunctionChoiceBehavior.auto(true))
                .build();
    }

    @Bean
    public ChatCompletionAgent chatCompletionAgent(Kernel kernel, InvocationContext invocationContext) {
        return ChatCompletionAgent.builder()
                .withName("SampleAssistantAgent")
                .withKernel(kernel)
                .withInvocationContext(invocationContext)
                .build();
    }
}
