package _store.envio.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import _store.envio.controller.EnvioController;
import _store.envio.model.Envio;

/**
 * Construye la representación hipermedia (HATEOAS) de un Envio: además de
 * los datos del recurso, agrega enlaces _links para que el cliente sepa qué
 * acciones puede hacer a continuación (autodescubrimiento de la API).
 */
@Component
public class EnvioModelAssembler implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {

    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        EntityModel<Envio> model = EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).buscarPorId(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).listarEnvios()).withRel("envios"));

        // Solo se puede confirmar un envío que todavía está PENDIENTE
        if (!"ENTREGADO".equals(envio.getEstadoEnvio())) {
            model.add(linkTo(methodOn(EnvioController.class).confirmarEntrega(envio.getId()))
                    .withRel("confirmar"));
        }

        model.add(linkTo(methodOn(EnvioController.class).eliminar(envio.getId())).withRel("eliminar"));

        return model;
    }
}
