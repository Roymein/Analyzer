package com.example.d2j.writer.item;

import com.example.d2j.writer.CodeWriter;
import com.example.d2j.writer.ann.Off;
import com.example.d2j.writer.insn.Insn;
import com.example.d2j.writer.insn.JumpOp;
import com.example.d2j.writer.insn.Label;
import com.example.d2j.writer.insn.PreBuildInsn;
import com.example.d2j.writer.io.DataOut;
import com.example.d2j.reader.Op;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class CodeItem extends BaseItem {

    public int registersSize;
    public int insSize;
    public int outsSize;
    public int insn_size;
    public List<TryItem> tries;
    @Off
    public DebugInfoItem debugInfo;
    public List<Insn> insns;
    public List<EncodedCatchHandler> handlers;

    List<TryItem> _tryItems;
    List<Insn> _ops;
    List<Insn> _tailOps;

    @Override
    public int place(int offset) {
        prepareInsns();
        prepareTries();

        offset += 16 + insn_size * 2;
        if (tries != null && tries.size() > 0) {
            if ((insn_size & 0x01) != 0) {// padding
                offset += 2;
            }
            offset += 8 * tries.size();
            if (handlers.size() > 0) {
                int base = offset;
                offset += lengthOfUleb128(handlers.size());

                for (EncodedCatchHandler h : handlers) {
                    h.handler_off = offset - base;
                    int size = h.addPairs.size();
                    offset += lengthOfSleb128(h.catchAll != null ? -size : size);
                    for (EncodedCatchHandler.AddrPair ap : h.addPairs) {
                        offset += lengthOfUleb128(ap.type.index) + lengthOfUleb128(ap.addr.offset);
                    }
                    if (h.catchAll != null) {
                        offset += lengthOfUleb128(h.catchAll.offset);
                    }
                }
            }

        }
        return offset;
    }

    @Override
    public void write(DataOut out) {
        out.ushort("registers_size", registersSize);
        out.ushort("ins_size", insSize);
        out.ushort("outs_size", outsSize);
        out.ushort("tries_size", tries == null ? 0 : tries.size());
        out.uint("debug_info_off", debugInfo == null ? 0 : debugInfo.offset);
        out.uint("insn_size", insn_size);
        ByteBuffer b = ByteBuffer.allocate(insn_size * 2).order(ByteOrder.LITTLE_ENDIAN);
        for (Insn insn : insns) {
            insn.write(b);
        }
        out.bytes("insn", b.array());
        if (tries != null && tries.size() > 0) {
            if ((insn_size & 0x01) != 0) {// padding
                out.skip("padding", 2);
            }
            int lastEnd = 0;
            for (TryItem ti : tries) {
                if (ti.start.offset < lastEnd) {
                    System.err.println("'Out-of-order try' may throwed by libdex");
                }
                out.uint("start_addr", ti.start.offset);
                out.ushort("insn_count", ti.end.offset - ti.start.offset);
                lastEnd = ti.end.offset;
                out.ushort("handler_off", ti.handler.handler_off);
            }
            if (handlers.size() > 0) {
                out.uleb128("size", handlers.size());
                for (EncodedCatchHandler h : handlers) {

                    int size = h.addPairs.size();
                    out.sleb128("size", (h.catchAll != null ? -size : size));
                    for (EncodedCatchHandler.AddrPair ap : h.addPairs) {
                        out.uleb128("type_idx", (ap.type.index));
                        out.uleb128("addr", (ap.addr.offset));
                    }
                    if (h.catchAll != null) {
                        out.uleb128("catch_all_addr", (h.catchAll.offset));
                    }
                }
            }
        }
    }

    public void init(List<Insn> ops, List<Insn> tailOps, List<TryItem> tryItems) {
        this._ops = ops;
        this._tailOps = tailOps;
        this._tryItems = tryItems;
    }

    private void prepareTries() {
        if (_tryItems.size() > 0) {
            List<TryItem> uniqTrys = new ArrayList<>();
            { // merge dup trys
                Set<TryItem> set = new HashSet<>();
                for (TryItem tryItem : _tryItems) {
                    if (!set.contains(tryItem)) {
                        uniqTrys.add(tryItem);
                        set.add(tryItem);
                    } else {
                        for (TryItem t : uniqTrys) {
                            if (t.equals(tryItem)) {
                                mergeExceptionHandler(t.handler, tryItem.handler);
                            }
                        }
                    }
                }
                set.clear();
                this.tries = uniqTrys;
                if (uniqTrys.size() > 0) {
                    Collections.sort(uniqTrys, new Comparator<TryItem>() {
                        @Override
                        public int compare(TryItem o1, TryItem o2) {
                            int x = o1.start.offset - o2.start.offset;
                            if (x == 0) {
                                x = o1.end.offset - o2.end.offset;
                            }
                            return x;
                        }
                    });
                }
            }
            { // merge dup handlers
                List<EncodedCatchHandler> uniqHanders = new ArrayList<>();
                Map<EncodedCatchHandler, EncodedCatchHandler> map = new HashMap<>();
                for (TryItem tryItem : uniqTrys) {
                    EncodedCatchHandler d = tryItem.handler;
                    EncodedCatchHandler uH = map.get(d);
                    if (uH != null) {
                        tryItem.handler = uH;
                    } else {
                        uniqHanders.add(d);
                        map.put(d, d);
                    }
                }
                this.handlers = uniqHanders;
                map.clear();
            }

        }
    }

    private void mergeExceptionHandler(EncodedCatchHandler to, EncodedCatchHandler from) {
        for (EncodedCatchHandler.AddrPair pair : from.addPairs) {
            if (!to.addPairs.contains(pair)) {
                to.addPairs.add(pair);
            }
        }
        if (to.catchAll == null) {
            to.catchAll = from.catchAll;
        }
    }

    private void prepareInsns() {
        List<JumpOp> jumpOps = new ArrayList<>();
        for (Insn insn : _ops) {
            if (insn instanceof CodeWriter.IndexedInsn) {
                ((CodeWriter.IndexedInsn) insn).fit();
            } else if (insn instanceof JumpOp) {
                jumpOps.add((JumpOp) insn);
            }
        }

        int codeSize = 0;
        while (true) {
            for (Insn insn : _ops) {
                insn.offset = codeSize;
                codeSize += insn.getCodeUnitSize();
            }
            boolean allfit = true;
            for (JumpOp jop : jumpOps) {
                if (!jop.fit()) {
                    allfit = false;
                }
            }
            if (allfit) {
                break;
            }
            codeSize = 0;
        }
        for (Insn insn : _tailOps) {
            if ((codeSize & 1) != 0) { // not 32bit alignment
                Insn nop = new PreBuildInsn(new byte[]{(byte) Op.NOP.opcode, 0}); // f10x
                insn.offset = codeSize;
                codeSize += nop.getCodeUnitSize();
                _ops.add(nop);
            }
            insn.offset = codeSize;
            codeSize += insn.getCodeUnitSize();
            _ops.add(insn);
        }
        _tailOps.clear();
        this.insns = _ops;
        this.insn_size = codeSize;
    }

    public static class EncodedCatchHandler {
        public int handler_off;
        public List<AddrPair> addPairs;
        public Label catchAll;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            EncodedCatchHandler that = (EncodedCatchHandler) o;

            if (!addPairs.equals(that.addPairs))
                return false;
            if (catchAll != null ? !catchAll.equals(that.catchAll) : that.catchAll != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = addPairs.hashCode();
            result = 31 * result + (catchAll != null ? catchAll.offset : 0);
            return result;
        }

        public static class AddrPair {
            final public TypeIdItem type;
            final public Label addr;

            public AddrPair(TypeIdItem type, Label addr) {
                this.type = type;
                this.addr = addr;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;

                AddrPair addrPair = (AddrPair) o;

                if (addr.offset != addrPair.addr.offset)
                    return false;
                if (!type.equals(addrPair.type))
                    return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = type.hashCode();
                result = 31 * result + addr.offset;
                return result;
            }
        }
    }

    public static class TryItem {
        public Label start;
        public Label end;
        public EncodedCatchHandler handler;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            TryItem tryItem = (TryItem) o;

            if (end.offset != tryItem.end.offset)
                return false;
            if (start.offset != tryItem.start.offset)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = start.offset;
            result = 31 * result + end.offset;
            return result;
        }
    }
}
