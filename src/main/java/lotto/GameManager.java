package lotto;

import static lotto.message.ErrorMessage.DIVISIBLE_BY_1000;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.Lotto;
import lotto.domain.Ranking;
import lotto.domain.WinningStatistics;
import lotto.view.Input;

public class GameManager {
    Input input = new Input();

    public List<Lotto> createLotto() {
        int purchaseQuantity = getPurchaseQuantity(input.getPurchaseAmount());
        List<Lotto> totalLotto = new ArrayList<>();

        IntStream.range(0, purchaseQuantity)
                .forEach(i -> {
                    List<Integer> lottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
                    Lotto lotto = new Lotto(lottoNumbers);
                    totalLotto.add(lotto);
                });

        return totalLotto;
    }

    private int getPurchaseQuantity(int purchaseAmount) {
        validationPurchaseAmountInThousands(purchaseAmount);
        return purchaseAmount / 1000;
    }

    private void validationPurchaseAmountInThousands(int purchaseAmount) {
        if (purchaseAmount % 1000 != 0) {
            throw new IllegalArgumentException(DIVISIBLE_BY_1000.errorMessage());
        }
    }

    public WinningStatistics compareLottoWithWinningNumbers(Lotto lotto, List<Integer> winningNumbers, int bonusNumber, WinningStatistics winningStatistics) {
        int matchCount = lotto.getNumbers().stream().filter(winningNumbers::contains).toList().size();

        updateWinningStatistics(lotto, matchCount, bonusNumber, winningStatistics);

        return winningStatistics;
    }

    private void updateWinningStatistics(Lotto lotto, int matchCount, int bonusNumber, WinningStatistics winningStatistics) {
        if (matchCount == 5) {
            winningStatistics.incrementWinningStatus(compareLottoWithBonusNumber(lotto, bonusNumber));
            return;
        }

        if (matchCount >= 3) {
            winningStatistics.incrementWinningStatus(Ranking.findByMatchCount(matchCount));
        }
    }

    private Ranking compareLottoWithBonusNumber(Lotto lotto, int bonusNumber) {
        for (int number : lotto.getNumbers()) {
            if (number == bonusNumber) {
                return Ranking.FIVE_MATCHES_BONUS_MATCHES;
            }
        }
        return Ranking.FIVE_MATCHES;
    }
}
