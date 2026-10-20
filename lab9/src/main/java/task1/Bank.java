package task1;

public class Bank {
    public boolean transfer(Account from, Account to, int amount){
        if(from == null || to == null) {
            throw new IllegalArgumentException("Рахунки не можуть бути null.");
        }

        if(amount <= 0) throw new IllegalArgumentException("Сума переказу повинна бути додатньою.");

        if(from == to) return true;

        Account first;
        Account second;

        if(from.getId() < to.getId()) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            synchronized (second) {
                if(from.getBalance() < amount) {
                    return false;
                }

                from.withdraw(amount);
                to.deposit(amount);

                return true;
            }
        }
    }
}