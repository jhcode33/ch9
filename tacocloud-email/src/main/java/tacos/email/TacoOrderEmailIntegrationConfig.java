package tacos.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

@Configuration
public class TacoOrderEmailIntegrationConfig {
  
  @Bean
  public IntegrationFlow tacoOrderEmailFlow(
      EmailProperties emailProps,
      EmailToOrderTransformer emailToOrderTransformer,
      OrderSubmitMessageHandler orderSubmitHandler) {
    
    return IntegrationFlows
        .from(Mail.imapInboundAdapter(emailProps.getImapUrl()), //gateway ( in bound adapter )
            e -> e.poller(
                Pollers.fixedDelay(emailProps.getPollRate())))  //gateway proxy
        .transform(emailToOrderTransformer)
        .handle(orderSubmitHandler) // out bound adapter
        .get();
  }
  
}