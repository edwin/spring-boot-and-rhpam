package com.edw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerStatus;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 *     com.edw.Main
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 25 Jan 2023 19:40
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Value("${kieserver.username}")
    private String user;

    @Value("${kieserver.password}")
    private String password;

    @Value("${kieserver.location}")
    private String url;

    @Value("${kjar.name}")
    private String kjarName;

    @Value("${kjar.groupid}")
    private String kjarGroupid;

    @Value("${kjar.artifactid}")
    private String kjarArtifactId;

    @Value("${kjar.version}")
    private String kjarVersion;

    @Bean
    CommandLineRunner deployAndValidate() {
        return new CommandLineRunner() {
            public void run(String... strings) throws Exception {
                KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, user, password, 60000);
                config.setMarshallingFormat(MarshallingFormat.JSON);

                KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
                KieContainerResource kContainer = new KieContainerResource();
                kContainer.setContainerId(kjarName);
                kContainer.setReleaseId(new ReleaseId(kjarGroupid, kjarArtifactId, kjarVersion));

                ServiceResponse<KieContainerResource> resp = client.createContainer(kjarName, kContainer);
                KieContainerStatus status = resp.getResult().getStatus();
                if (!KieContainerStatus.STARTED.equals(status)) {
                    throw new IllegalStateException();
                }
            }
        };
    }
}
