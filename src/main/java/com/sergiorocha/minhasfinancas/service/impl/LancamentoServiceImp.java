package com.sergiorocha.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergiorocha.minhasfinancas.exception.RegraNegocioException;
import com.sergiorocha.minhasfinancas.model.entity.Lancamento;
import com.sergiorocha.minhasfinancas.model.enums.StatusLancamento;
import com.sergiorocha.minhasfinancas.model.repository.LancamentoRepository;
import com.sergiorocha.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImp implements LancamentoService{

	private LancamentoRepository repository;
	
	public LancamentoServiceImp(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamento) {
		//para montar a query pelos objetos preenchidos
		Example example = Example.of(lancamento,ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
				
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);		
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if (lancamento.getDescricao()== null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descricao v??lida.");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes()< 1 || lancamento.getMes()> 12) {
			throw new RegraNegocioException("Informe um M??s v??lida.");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano v??lida.");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usu??rio v??lida.");
		}
		
		if (lancamento.getValor() ==null || lancamento.getValor().compareTo(BigDecimal.ZERO)< 1) {
			throw new RegraNegocioException("Informe um Valor v??lida.");
		}
		
		if (lancamento.getTipo() == null ) {
			throw new RegraNegocioException("Informe um Tipo de Lancamento v??lida.");
		}
	}

}
