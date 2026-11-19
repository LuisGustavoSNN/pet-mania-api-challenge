package com.dt2.petmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dt2.petmania.repositories.PetRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:petmania_test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=false"
})
class PetControllerIntegrationTests {

    @LocalServerPort
    private int porta;

    @Autowired
    private PetRepository repository;

    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void fluxoDeCadastroConsultaAtualizacao() throws Exception {
        HttpResponse<String> vazia = requisitar("GET", "/api/pets", null);
        assertEquals(200, vazia.statusCode());
        assertEquals("[]", vazia.body());

        String dados = """
                {"nome":"Luna","especie":"Gato","raca":"Siamês","idade":2,
                "sexo":"Fêmea","porte":"Pequeno","personalidade":"Calma",
                "foto":"","disponivel":true}
                """;
        HttpResponse<String> cadastro = requisitar("POST", "/api/pets", dados);
        assertEquals(201, cadastro.statusCode());
        assertTrue(cadastro.body().contains("\"nome\":\"Luna\""));
        Matcher matcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(cadastro.body());
        assertTrue(matcher.find());
        String id = matcher.group(1);

        HttpResponse<String> consulta = requisitar("GET", "/api/pets/" + id, null);
        assertEquals(200, consulta.statusCode());
        assertTrue(consulta.body().contains("Luna"));
        assertTrue(requisitar("GET", "/api/pets", null).body().contains("Luna"));

        String atualizacao = """
                {"nome":"Luna","especie":"Gato","raca":"Siamês","idade":3,
                "sexo":"Fêmea","porte":"Pequeno","personalidade":"Brincalhona",
                "foto":"","disponivel":false}
                """;
        HttpResponse<String> editado = requisitar("PUT", "/api/pets/" + id, atualizacao);
        assertEquals(200, editado.statusCode());
        assertTrue(editado.body().contains("Brincalhona"));
        assertTrue(editado.body().contains("\"disponivel\":false"));

    }

    @Test
    void petInexistenteERespostaInvalida() throws Exception {
        assertEquals(404, requisitar("GET", "/api/pets/9999999", null).statusCode());

        String invalido = """
                {"nome":"","especie":"Cachorro","idade":-1,
                "sexo":"Macho","porte":"Grande","disponivel":true}
                """;
        assertEquals(400, requisitar("POST", "/api/pets", invalido).statusCode());
    }

    private HttpResponse<String> requisitar(String metodo, String caminho, String corpo)
            throws Exception {
        HttpRequest.Builder requisicao = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + porta + caminho))
                .header("Accept", "application/json");

        if (corpo == null) {
            requisicao.method(metodo, HttpRequest.BodyPublishers.noBody());
        } else {
            requisicao.header("Content-Type", "application/json; charset=UTF-8")
                    .method(metodo, HttpRequest.BodyPublishers.ofString(corpo, StandardCharsets.UTF_8));
        }

        return client.send(requisicao.build(), HttpResponse.BodyHandlers.ofString());
    }
}
