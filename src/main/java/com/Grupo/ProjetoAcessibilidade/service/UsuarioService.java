package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.UsuarioDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceAlreadyExistsException;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PapelService papelService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PapelService papelService
                          ) {
        this.usuarioRepository = usuarioRepository;
        this.papelService = papelService;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado com ID: " + id));
    }

    public Usuario salvar(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado.");
        }
        if (usuarioRepository.existsByCpf(dto.cpf())) {
            throw new ResourceAlreadyExistsException("CPF já cadastrado.");
        }

        Papel papel = papelService.buscarPorId(dto.papelId());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setTelefone(dto.telefone());

        usuario.setPapel(papel);

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(String id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);

        Papel papel = papelService.buscarPorId(dto.papelId());

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setTelefone(dto.telefone());
        usuario.setPapel(papel);


        return usuarioRepository.save(usuario);
    }

    public void deletar(String id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}