# springboot-elastic-agent

## Présentation
Le projet *springboot-elastic-agent* est la librairie pour utiliser l'agent de monitoring Elastic dans les projets Spring Boot.

## Frameworks
- [Spring boot](https://spring.io/projects/spring-boot) [@3.5.14](https://docs.spring.io/spring-boot/3.5/index.html)
    - [Spring Boot Security](https://spring.io/projects/spring-security)

## Dependencies
- [Commons Core](https://github.com/flc-it/commons-core)
- [Elastic APM Java Agent](https://www.elastic.co/guide/en/apm/agent/java/index.html)

## Fonctionnalités

Permet à l'API d'envoyer ses metrics à l'APM Elastic qui les enregistre dans Elasticsearch et sont remontés en temps réel dans Kibana.
  
Documentation : https://www.elastic.co/fr/observability/application-performance-monitoring
  
Ajout automatique du context user depuis le User authentifié dans Spring Security.
## Implementation du Bean d'injection du context dans Elastic

Ajout automatique du context dans Elastic depuis la classe de Bean implmentée :
**org.flcit.springboot.elastic.agent.context.ElasticSetContext**

FlcElasticSetContext.class :
```java
import org.flcit.springboot.elastic.agent.context.ElasticSetContext;
import org.flcit.springboot.elastic.agent.context.ElasticUserContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
public class FlcElasticSetContext implements ElasticSetContext {

    @Override
    public ElasticUserContext getUserContext(Authentication authentication) {
        if (authentication instanceof OAuth2TokenAuthenticationToken oauth2Token) {
            return new ElasticUserContext()
                    .setUsername(oauth2Token.getName());
        }
        return null;
    }

}
```

## Paramétrage de l'agent Elastic java

Reprends la configuration native de la librairie de l'elastic APM Java :
https://www.elastic.co/guide/en/apm/agent/java/current/configuration.html
  
Url du server Elastic APM qui va récupérer les metrics et les enregistrer dans Elasticsearch
```properties
elastic.apm.server-url=https://elasticapm
```

Nom du service
```properties
elastic.apm.service-name=@project.artifactId@
```

Environnement de l'instance
```properties
elastic.apm.environment=${server.exec.environment}
```

Packages de l'application 
```properties
elastic.apm.application-packages=@project.groupId@
```

Hostname de l'instance
```properties
elastic.apm.hostname=${server.exec.hostname}
```

Pour capturer les body reçus en entrée de l'API
```properties
elastic.apm.capture-body=all # capture only the received body
```

Niveau de log de la librairie d'APM
```properties
elastic.apm.log-level=debug
```

## APM Server

Pour configurer un APM Server en local => https://www.elastic.co/fr/downloads/apm
les fichiers de configuration de la suite Elastic (Elasticsearch, heartbeat, APM Server, Kibana) pour une installation en local se trouve dans le répertoire elastic-conf.

## Configuration d'un front Angular

Permet à un front d'être monitoré en temps réel dans la suite Elastic (Elasticsearch / Kibana).
  
Documentation d'intégration : [Elastic APM Agent Angular Documentation](https://www.elastic.co/guide/en/apm/agent/rum-js/current/angular-integration.html)
  
Utilisation de la librairie Angular officielle [@elastic/apm-rum-angular](https://www.npmjs.com/package/@elastic/apm-rum-angular).

### Initialisation de l'APM dans le module Angular

```typescript
    providers: [

        ApmService,
        {
            provide: ErrorHandler,
            useClass: ApmErrorHandler
        }
    ]

export class AppModule {

    constructor(private readonly serviceApm: ApmService) {
        // Agent API is exposed through this apm instance if config active and found
        if (environment.apm
            && environment.apm.active
            && environment.apm.serverUrl) {
            this.serviceApm.init({
                serviceName: environment.apm.serviceName,
                serverUrl: environment.apm.serverUrl,
                environment: environment.env
            });
        }
  }

}
```

### Ajout automatique de l'utilisateur connecté dans le context User de l'APM

```typescript
private setCurrentUser(user: User) {
      if (user !== this.currentUser) {
          this.currentUser = user;
          this._user.next(this.currentUser);
          if (this.serviceApm.apm.isActive()) {
              this.serviceApm.apm.setUserContext(SessionService.map(user));
          }
      }
}
```

### Paramétrage

```javascript
apm: {
      active: false,
      serviceName: 'api-manager-front',
      serverUrl: 'https://elasticapm',
}
```

### Exemples d'implémentation
- Projet Secure Store Manager => [SSM GitHub](https://github.com/flc-it/secure-store-manager-back)
- Projet API Manager => [APIM GitHub](https://github.com/flc-it/api-manager-back)