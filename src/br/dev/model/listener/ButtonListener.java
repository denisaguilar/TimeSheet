package br.dev.model.listener;

import java.util.EventListener;

public interface ButtonListener extends EventListener {
	public boolean onCheckout();

	public boolean onCheckout(boolean preloaded);

	public boolean onCheckin();

	public boolean onCheckin(boolean preloaded);

	public boolean onNewTimeSheet();

	public boolean onNewTimeSheet(boolean preloaded);

	public void dailyBackup();

	public void monthlyBackup();

}
