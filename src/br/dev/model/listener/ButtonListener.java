package br.dev.model.listener;

import java.util.EventListener;

public interface ButtonListener extends EventListener {
	public boolean onCheckout();
	public boolean onCheckin();
	public boolean onNewTimeSheet();
}
