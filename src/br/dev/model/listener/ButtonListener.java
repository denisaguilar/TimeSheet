package br.dev.model.listener;

import java.util.EventListener;

public interface ButtonListener extends EventListener {
	public boolean onCheckout();
	public boolean onCheckout(long value);
	public boolean onCheckin();
	public boolean onCheckin(long value);
	public boolean onNewTimeSheet();
	public boolean onNewTimeSheet(boolean preloaded);
}
