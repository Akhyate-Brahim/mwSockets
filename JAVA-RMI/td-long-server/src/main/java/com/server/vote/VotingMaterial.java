package com.server.vote;

import com.common.exceptions.AlreadyUsedOTPException;
import com.common.exceptions.IncorrectScoreException;
import com.common.vote.IVotingMaterial;
import com.server.adminApp.AdminApp;
import com.server.user.OTPCreator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotingMaterial extends UnicastRemoteObject implements IVotingMaterial {
    AdminApp adminApp;
    private final ConcurrentHashMap<Integer, Integer> votes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> otpsUsed = new ConcurrentHashMap<>();
    public VotingMaterial(AdminApp adminApp) throws RemoteException {
        this.adminApp=adminApp;
    }

    @Override
    public String getOTP() throws RemoteException {
        String otp = OTPCreator.generate();
        while (otpsUsed.containsKey(otp)) {
            otp = OTPCreator.generate();
        }
        otpsUsed.put(otp, false);
        return otp;
    }



    public void castVote(Map<Integer, Integer> candidateScores, String otp)
            throws RemoteException, AlreadyUsedOTPException, IncorrectScoreException {
        if (Boolean.TRUE.equals(otpsUsed.getOrDefault(otp, true))) {
            throw new AlreadyUsedOTPException("This OTP has already been used or is invalid.");
        }

        for (Map.Entry<Integer, Integer> entry : candidateScores.entrySet()) {
            int candidateId = entry.getKey();
            int score = entry.getValue();

            if (score < 0 || score > 3) {
                throw new IncorrectScoreException("Score for candidate " + candidateId + " is out of range.");
            }

            votes.compute(candidateId, (key, val) -> val == null ? score : val + score);
        }
        otpsUsed.put(otp, true);  // Marking the OTP as used after casting the vote
    }
}
