package com.claro.pedidos.service;

import com.claro.pedidos.dto.PedidoRequest;
import com.claro.pedidos.dto.PedidoResponse;
import com.claro.pedidos.entity.Pedido;
import com.claro.pedidos.entity.StatusPedido;
import com.claro.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final int LIMITE_MAXIMO_PEDIDOS = 5;

    private final PedidoRepository pedidoRepository;

    public List<PedidoResponse> getAllPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PedidoResponse getPedidoById(UUID pedidoUUID) {
        Pedido pedido = findPedidoById(pedidoUUID);

        return toResponse(pedido);
    }

    public PedidoResponse createPedido(PedidoRequest request) {
        if (pedidoRepository.count() >= LIMITE_MAXIMO_PEDIDOS) {
            throw new RuntimeException(
                    "O limite máximo de 5 pedidos foi atingido"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setDisplayName(request.displayName());
        pedido.setItens(request.itens());
        pedido.setPeso(request.peso());
        pedido.setStatus(StatusPedido.EM_PROCESSAMENTO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return toResponse(pedidoSalvo);
    }

    public void deletePedido(UUID pedidoUUID) {
        Pedido pedido = findPedidoById(pedidoUUID);

        pedidoRepository.delete(pedido);
    }

    private Pedido findPedidoById(UUID pedidoUUID) {
        return pedidoRepository.findById(pedidoUUID)
                .orElseThrow(() ->
                        new RuntimeException("Pedido não encontrado")
                );
    }

    private PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getDisplayName(),
                pedido.getItens(),
                pedido.getPeso(),
                pedido.getStatus()
        );
    }
}