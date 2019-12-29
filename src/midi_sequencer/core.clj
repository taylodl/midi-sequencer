(ns midi-sequencer.core
  (:gen-class))

(declare play)
(declare getSynthPlayer)

(def PITCHES
     {:C0 12 :C0# 13 :D0b 13 :D0 14 :D0# 15 :E0b 15 :E0 16 :F0 17 :F0# 18 :G0b 18 :G0 19 :G0# 20 :A0b 20 :A0 21 :A0# 22 :B0b 22 :B0 23
      :C1 24 :C1# 25 :D1b 25 :D1 26 :D1# 27 :E1b 27 :E1 28 :F1 29 :F1# 30 :G1b 30 :G1 31 :G1# 32 :A1b 32 :A1 33 :A1# 34 :B1b 34 :B1 35
      :C2 36 :C2# 37 :D2b 37 :D2 38 :D2# 39 :E2b 39 :E2 40 :F2 41 :F2# 42 :G2b 42 :G2 43 :G2# 44 :A2b 44 :A2 45 :A2# 45 :B2b 46 :B2 47
      :C3 48 :C3# 49 :D3b 49 :D3 50 :D3# 51 :E3b 51 :E3 52 :F3 53 :F3# 54 :G3b 54 :G3 55 :G3# 56 :A3b 56 :A3 57 :A3# 58 :B3b 58 :B3 59
      :C4 60 :C4# 61 :D4b 61 :D4 62 :D4# 63 :E4b 63 :E4 64 :F4 65 :F4# 66 :G4b 66 :G4 67 :G4# 68 :A4b 68 :A4 69 :A4# 70 :B4b 70 :B4 71
      :C5 72 :C5# 73 :D5b 73 :D5 74 :D5# 75 :E5b 75 :E5 76 :F5 77 :F5# 78 :G5b 78 :G5 79 :G5# 80 :A5b 80 :A5 81 :A5# 82 :B5b 82 :B5 83
      :C6 84 :C6# 85 :D6b 85 :D6 86 :D6# 87 :E6b 87 :E6 88 :F6 89 :F6# 90 :G6b 90 :G6 91 :G6# 92 :A6b 92 :A6 93 :A6# 94 :B6b 94 :B6 95
      :C7 96 :C7# 97 :D7b 97 :D7 98 :D7# 99 :E7b 99 :E7 100 :F7 101 :F7# 102 :G7b 102 :G7 103 :G7# 104 :A7b 104 :A7 105 :A7# 106 :B7b 106 :B7 107
      :C8 108 :C8# 109 :D8b 109 :D8 110 :D8# 111 :E8b 111 :E8 112 :F8 113 :F8# 114 :G8b 114 :G8 115 :G8# 116 :A8b 116 :A8 117 :A8# 118 :B8b 118 :B8 119})

(def CMaj
     '(:CO :D0 :E0 :F0 :G0 :A0 :B0
       :C1 :D1 :E1 :F1 :G1 :A1 :B1
       :C2 :D2 :E2 :F2 :G2 :A2 :B2
       :C3 :D3 :E3 :F3 :G3 :A3 :B3
       :C4 :D4 :E4 :F4 :G4 :A4 :B4
       :C5 :D5 :E5 :F5 :G5 :A5 :B5
       :C6 :D6 :E6 :F6 :G6 :A6 :B6
       :C7 :D7 :E7 :F7 :G7 :A7 :B7
       :C8 :D8 :E8 :F8 :G8 :A8 :B8))

(def FMaj
     '(:F0 :G0 :A0 :B0b :C1 :D1 :E1
       :F1 :G1 :A1 :B1b :C2 :D2 :E2
       :F2 :G2 :A2 :B2b :C3 :D3 :E3
       :F3 :G3 :A3 :B3b :C4 :D4 :E4
       :F4 :G4 :A4 :B4b :C5 :D5 :E5
       :F5 :G5 :A5 :B5b :C6 :D6 :E6
       :F6 :G6 :A6 :B6b :C7 :D7 :E7
       :F7 :G7 :A7 :B7b :C8 :D8 :E8))

(def NOTES
     {:SIXTEENTH 1/4 :DOTTED-SIXTEENTH 3/8 :EIGHTH 1/2 :DOTTED-EIGHTH 3/4 :QUARTER 1 :DOTTED-QUARTER 3/2 :DOUBLE 2 :DOTTED-DOUBLE 3 :WHOLE 4 :DOTTED-WHOLE 6})

(def chromatic-scale 
     '([:C4 :QUARTER][:C4# :QUARTER][:D4 :QUARTER][:D4# :QUARTER][:E4 :QUARTER][:F4 :QUARTER]
       [:F4# :QUARTER][:G4 :QUARTER][:G4# :QUARTER][:A4 :QUARTER][:A4# :QUARTER][:B4 :QUARTER][:C5 :QUARTER]
       [:C5 :QUARTER][:B4 :QUARTER][:B4b :QUARTER][:A4 :QUARTER][:A4b :QUARTER][:G4 :QUARTER][:G4b :QUARTER]
       [:F4 :QUARTER][:E4 :QUARTER][:E4b :QUARTER][:D4 :QUARTER][:D4b :QUARTER] [:C4 :DOTTED-WHOLE]))


(def joy-to-the-world
     '([:F5 :QUARTER][:E5 :DOTTED-EIGHTH][:D5 :SIXTEENTH][:C5 :DOTTED-QUARTER][:B4b :EIGHTH]
       [:A4 :QUARTER][:G4 :QUARTER][:F4 :DOTTED-QUARTER][:C5 :EIGHTH][:D5 :DOTTED-QUARTER][:D5 :EIGHTH]
       [:E5 :DOTTED-QUARTER][:E5 :EIGHTH][:F5 :DOTTED-QUARTER][:F5 :EIGHTH][:F5 :EIGHTH ][:E5 :EIGHTH][:D5 :EIGHTH ][:C5 :EIGHTH]
       [:C5 :DOTTED-EIGHTH ][:B4b :SIXTEENTH ][:A4 :EIGHTH][:F5 :EIGHTH][:F5 :EIGHTH ][:E5 :EIGHTH][:D5 :EIGHTH ][:C5 :EIGHTH][:C5 :DOTTED-EIGHTH ][:B4b :SIXTEENTH ][:A4 :EIGHTH][:A4 :EIGHTH]
       [:A4 :EIGHTH][:A4 :EIGHTH][:A4 :EIGHTH][:A4 :SIXTEENTH ][:B4b :SIXTEENTH][:C5 :DOTTED-QUARTER][:B4b :SIXTEENTH ][:A4 :SIXTEENTH]
       [:G4 :EIGHTH][:G4 :EIGHTH][:G4 :EIGHTH][:G4 :SIXTEENTH ][:A4 :SIXTEENTH][:B4b :DOTTED-QUARTER][:A4 :SIXTEENTH ][:G4 :SIXTEENTH][:F4 :EIGHTH][:F5 :QUARTER ][:D5 :EIGHTH]
       [:C5 :DOTTED-EIGHTH ][:B4b :SIXTEENTH ][:A4 :EIGHTH][:B4b :EIGHTH][:A4 :QUARTER][:G4 :QUARTER][:F4 :WHOLE]))


(def interval-2nd (memoize (fn [pitch scale] (first (rest (drop-while #(not (= pitch %)) scale))))))
(def interval-3rd (memoize (fn [pitch scale] (first (rest (rest (drop-while #(not (= pitch %)) scale)))))))
(def interval-4th (memoize (fn [pitch scale] (first (rest (rest (rest (drop-while #(not (= pitch %)) scale))))))))
(def interval-5th (memoize (fn [pitch scale] (first (rest (rest (rest (rest (drop-while #(not (= pitch %)) scale)))))))))
(def interval-6th (memoize (fn [pitch scale] (first (rest (rest (rest (rest (rest (drop-while #(not (= pitch %)) scale))))))))))
(def interval-7th (memoize (fn [pitch scale] (first (rest (rest (rest (rest (rest (rest (drop-while #(not (= pitch %)) scale)))))))))))

(def triad (memoize (fn [pitch scale] (vector pitch (interval-3rd pitch scale) (interval-5th pitch scale)))))

(defn -main
  "Right now we play the chromatic scale using the instrument provided"
  [instrument & args]
  (let [synthPlayer (getSynthPlayer 2 (Integer/parseInt instrument))]
       (play synthPlayer 80 90 joy-to-the-world)))



(defn play [synthPlayer volume bpm seqMidiNotes]
  (doseq [midiNote seqMidiNotes]
	 (let [[pitch  note {:keys [vol_adj tie] :or {vol_adj 0}}] midiNote] 
	      (synthPlayer 
	       (+ volume vol_adj) 
	       (map #(PITCHES %) (triad pitch FMaj))
	       (/ (* 60000 (NOTES note)) bpm)))))



(defn getSynthPlayer [channelNbr instrumentNbr]
  "Initialize synthesizer and return play function"
  (let [synth (javax.sound.midi.MidiSystem/getSynthesizer)]
       (do
	(.open synth) ; Must open synth before we can do anything with it
	(let [channels (.getChannels synth)
	     instruments (.. synth getDefaultSoundbank getInstruments)]
	     (do 
	      (let [channel (nth channels channelNbr)
		   instrument (nth instruments instrumentNbr)]
		   (println "Instrument" instrumentNbr "is" (.getName instrument))
		   (.loadInstrument synth instrument)
		   (.programChange channel instrumentNbr) ; Lots of blogs never mentioned having to do this!!!
		   (fn [volume pitches duration] ; play function
		       (do
			(doseq [pitch pitches] (.noteOn channel pitch volume))
			(Thread/sleep duration)
			(doseq [pitch pitches] (.noteOff channel pitch))))))))))



