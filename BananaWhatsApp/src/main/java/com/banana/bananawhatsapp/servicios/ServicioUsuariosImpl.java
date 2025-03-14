package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServicioUsuariosImpl implements IServicioUsuarios{

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario obtener(int id) throws UsuarioException {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) throws UsuarioException {
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean borrarUsuario(Usuario usuario) throws UsuarioException {
        usuarioRepository.delete(usuario);

        return usuarioRepository.findById(usuario.getId()).isPresent() ? false : true;
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws UsuarioException {
        return usuarioRepository.save(usuario);
    }

    @Override
    public List <Usuario> obtenerPosiblesDesinatarios(Usuario usuario, int max) throws UsuarioException {
        Usuario us = Usuario.builder().activo(true).build();

        List<Usuario> destinatarios = usuarioRepository.findAll(Example.of(us)).stream().filter(u -> !u.getId().equals(usuario.getId())).collect(Collectors.toList());

        return destinatarios;
    }
}
