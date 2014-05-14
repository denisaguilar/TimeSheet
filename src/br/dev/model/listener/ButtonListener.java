package br.dev.model.listener;

import java.util.EventListener;

public interface ButtonListener extends EventListener {
	public boolean onCheckout();
	public void onCheckin();
	public void onNewTimeSheet();
}
