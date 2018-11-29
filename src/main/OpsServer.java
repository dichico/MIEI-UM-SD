/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author SarahTifanydaSilva
 */
public enum OpsServer {
	//operaÃ§Ãµes sobre o banco
	SING_IN,
	FECHAR_CONTA,
	CONSULTAR,
	CONSULTAR_TOTAL,
	LEVANTAR,
	DEPOSITAR,
	TRANSFERIR,
	
	//estado da operaÃ§Ã£o
	OK,
	OP_INVALIDA,
	SALDO_INSUFICIENTE,
	CONTA_INVALIDA;
}
